package com.paypay.paypaytest.presentation.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.paypay.paypaytest.R
import com.paypay.paypaytest.data.db.CurrencyDBDatabase
import com.paypay.paypaytest.data.db.CurrencyDetailsModel
import com.paypay.paypaytest.presentation.ApiWorker
import kotlinx.coroutines.delay
import java.time.Duration
import java.util.concurrent.TimeUnit

class CurrencyViewModel(context: Application) : AndroidViewModel(context) {
    var database: CurrencyDBDatabase? = CurrencyDBDatabase.getDataBase(context)
    var dbCurrencyList: List<CurrencyDetailsModel>? = null
    var countryArray = MutableLiveData<Array<CurrencyDetailsModel>>(emptyArray())
    var amount = MutableLiveData<String>()
    var listCurrencyLiveData: MutableLiveData<MutableList<RowViewModel>> = MutableLiveData()
    var isLoading = MutableLiveData(false)
    private val listRow = mutableListOf<RowViewModel>()
    var selectedCountry: MutableLiveData<CurrencyDetailsModel> = MutableLiveData(null)

    init {

        //Worker To sync api detailer every 30 min
        WorkManager.getInstance(context).cancelAllWork()
        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .build()

        val workRequest: PeriodicWorkRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PeriodicWorkRequest.Builder(
                ApiWorker::class.java,
                Duration.ofMinutes(30)
            ).setConstraints(myConstraints)
                .addTag(ApiWorker.TAG)
                .build()

        } else {
            PeriodicWorkRequest.Builder(
                ApiWorker::class.java,
                30,TimeUnit.MINUTES
            ).setConstraints(myConstraints)
                .addTag(ApiWorker.TAG)
                .build()
        }

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                ApiWorker.TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )

    }

    /*
    * Calculating & displaying converted currencies
    * */
    suspend fun getListConvertedList(amount: Float, currencyShortForm: String) {
        isLoading.value = true
        val selectedCurrency = database!!.currencyDao().getCurrencyByShortForm(currencyShortForm)
        listRow.clear()
        dbCurrencyList?.forEachIndexed { index, currencyDetailsModel ->
            val convertedAmount =
                amount * (currencyDetailsModel.currencyValue / selectedCurrency.currencyValue)
            val color = if (index % 2 != 0) {
                R.color.darker_gray
            } else {
                R.color.white
            }

            listRow.add(
                RowViewModel(
                    color,
                    currencyDetailsModel.countryName,
                    currencyDetailsModel.currencyShortForm,
                    String.format("%.4f", convertedAmount)
                )
            )
        }
        delay(1000) //to display loading for extra time
        listCurrencyLiveData.value = listRow
        isLoading.value = false
    }
}