package com.paypay.network

import android.util.Log
import com.paypay.common.model.CurrencyValueModel
import com.paypay.common.utils.Constants
import com.paypay.common.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/*
* All the api calls should handled here
* */
class RetrofitRepository {
    val retrofitService = RetrofitFactory.makeRetrofitService()
    var apiEvent = SingleLiveEvent<CurrencyApiEvents>()

    private object HOLDER {
        val INSTANCE = RetrofitRepository()
    }

    companion object {
        val instance: RetrofitRepository by lazy { HOLDER.INSTANCE }
    }

    /*
    * Api call to get all currencies name
    * */
    suspend fun getCurrencyListFromServer() {

        retrofitService.getAllCurrenciesName().enqueue(object : Callback<HashMap<String, String>> {

            override fun onResponse(
                call: Call<HashMap<String, String>>,
                response: Response<HashMap<String, String>>
            ) {
                try {

                    response.body()?.let {
                        apiEvent.value= CurrencyApiEvents.ListApiSuccess(it)
                    }
                } catch (t: Throwable) {
                    //set null list in case of crash
                    apiEvent.value= CurrencyApiEvents.ListApiSuccess("Data Not Found")
                    t.printStackTrace()


                }
            }

            override fun onFailure(call: Call<HashMap<String, String>>, t: Throwable) {
//                set null list in case of failure
                apiEvent.value= CurrencyApiEvents.ListApiSuccess("Data Not Found")
                t.printStackTrace()

            }
        })

    }

    /*
    * Api call to get all currencies name
    * */
    suspend fun getCurrencyValueListFromServer() {
        retrofitService.getAllCurrenciesValue(Constants.API_KEY)
            .enqueue(object : Callback<CurrencyValueModel> {

                override fun onResponse(
                    call: Call<CurrencyValueModel>,
                    response: Response<CurrencyValueModel>
                ) {
                    try {
                    response.body()?.let {
                        apiEvent.value= CurrencyApiEvents.ValueApiSuccess(it)
                    }
                    } catch (t: Throwable) {
                        //set null list in case of crash
                        apiEvent.value= CurrencyApiEvents.ValueApiFailure("Data Not Found")
                        t.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<CurrencyValueModel>, t: Throwable) {
                    //set null list in case of failure
                    apiEvent.value= CurrencyApiEvents.ValueApiFailure("Data Not Found")
                    t.printStackTrace()
                }
            })
    }
}





