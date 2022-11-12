package com.paypay.paypaytest.presentation

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.paypay.common.model.CurrencyValueModel
import com.paypay.common.utils.Constants
import com.paypay.network.CurrencyApiEvents
import com.paypay.network.RetrofitRepository
import com.paypay.paypaytest.data.db.CurrencyDBDatabase
import com.paypay.paypaytest.data.db.CurrencyDetailsModel
import kotlinx.coroutines.*

/*
* API call through Work Manager
* */
class ApiWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private var database: CurrencyDBDatabase? = CurrencyDBDatabase.getDataBase(context)
    private var hashMap = HashMap<String, String>()
    private var dbCurrencyList: List<CurrencyDetailsModel>? =
        database!!.currencyDao().getALLDetails().value
    var countryArray = MutableLiveData<Array<CurrencyDetailsModel>>(emptyArray())


    companion object {
        const val TAG = "ApiWorker"
    }

    override fun doWork(): Result {
        apiCallingAndDBHandling()
        return Result.success()
    }

    // Api calling using Coroutines
    private fun apiCallingAndDBHandling() {
        GlobalScope.launch(Dispatchers.IO) {
            //If country list present then only currency list api will be called.
            dbCurrencyList?.let { it ->
                dbCurrencyList = it.sortedBy { details -> details.countryName }
                countryArray.postValue(it.toTypedArray())
                hashMap = HashMap()
                it.forEach { details ->
                    hashMap.put(details.currencyShortForm, details.countryName)
                }
                RetrofitRepository.instance.getCurrencyValueListFromServer()
            } ?: kotlin.run {
                async {
                    RetrofitRepository.instance.getCurrencyListFromServer()
                }
            }

        }
        GlobalScope.launch(Dispatchers.Main) {
            RetrofitRepository.instance.apiEvent.observeForever {
                when (it) {
                    is CurrencyApiEvents.ListApiSuccess -> {
                        hashMap = it.data as HashMap<String, String>
                        runBlocking {
                            RetrofitRepository.instance.getCurrencyValueListFromServer()
                        }
                    }
                    is CurrencyApiEvents.ValueApiSuccess -> {
                        val currencyValueModel = it.data as CurrencyValueModel
                        val currencyList = ArrayList<CurrencyDetailsModel>()

                        dbCurrencyList?.let {
                            runBlocking {
                                async {
                                    it.forEach { dbCurrency ->
                                        database!!.currencyDao().update(
                                            CurrencyDetailsModel(
                                                countryName = dbCurrency.countryName,
                                                currencyShortForm = dbCurrency.currencyShortForm,
                                                currencyValue = currencyValueModel.rates[dbCurrency.currencyShortForm]!!,
                                                updatedAt = System.currentTimeMillis()
                                            )
                                        )
                                    }
                                    setDbList()
                                    ""
                                }.await()
                            }
                        } ?: kotlin.run {
                            currencyValueModel.rates.forEach { entry ->
                                val countryName = hashMap[entry.key] ?: ""
                                if (!TextUtils.isEmpty(countryName)) {
                                    currencyList.add(
                                        CurrencyDetailsModel(
                                            countryName = countryName,
                                            currencyShortForm = entry.key,
                                            currencyValue = entry.value,
                                            updatedAt = System.currentTimeMillis()
                                        )
                                    )
                                }
                            }

                            runBlocking {
                                async {
                                    if (!currencyList.isNullOrEmpty()) {
                                        database!!.currencyDao().insertList(currencyList)
                                    }
                                    setDbList()
                                    ""
                                }.await()
                            }
                        }
                    }
                }
            }
        }

    }

    // Sorting of list
    private suspend fun setDbList() {
        dbCurrencyList = database!!.currencyDao().getALLDetails().value
        dbCurrencyList?.let {
            dbCurrencyList = it.sortedBy { details -> details.countryName }
            countryArray.postValue(it.toTypedArray())
        }

    }

}