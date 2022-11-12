package com.paypay.paypaytest.data.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paypay.common.model.CurrencyValueModel

class RequestViewModel : ViewModel() {
    private val countryResult = MutableLiveData<HashMap<String, String>>()
    private val currencyResult = MutableLiveData<CurrencyValueModel>()

    fun getCountryResult(): MutableLiveData<HashMap<String, String>> {

        return countryResult
    }

    fun getCurrencyResult(): MutableLiveData<CurrencyValueModel> {
        return currencyResult
    }
}