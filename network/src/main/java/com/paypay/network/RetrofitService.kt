package com.paypay.network

import com.paypay.common.model.CurrencyValueModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
* Retrofit Api Service
* */
interface RetrofitService {

    // Get All Details Calls
    @GET("currencies.json")
    fun getAllCurrenciesName(): Call<HashMap<String, String>>

    @GET("latest.json")
    fun getAllCurrenciesValue(@Query("app_id") apiKey: String): Call<CurrencyValueModel>
}