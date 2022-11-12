package com.paypay.common.model

data class CurrencyValueModel(
    var disclaimer:String,
    var license:String,
    var timestamp:Long,
    var base:String,
    var rates:Map<String, Double>
)