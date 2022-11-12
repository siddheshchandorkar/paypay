package com.paypay.network

sealed class CurrencyApiEvents {

    data class ListApiSuccess(val data: Any) : CurrencyApiEvents()
    data class ListApiFailure(val errorMessage: Any) : CurrencyApiEvents()
    data class ValueApiSuccess(val data: Any) : CurrencyApiEvents()
    data class ValueApiFailure(val errorMessage: Any) : CurrencyApiEvents()

}