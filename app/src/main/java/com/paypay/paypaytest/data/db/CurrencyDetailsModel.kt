package com.paypay.paypaytest.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_details")
data class CurrencyDetailsModel(
    @PrimaryKey
    @ColumnInfo(name = "currencyShortForm")
    var currencyShortForm: String = "",
    @ColumnInfo(name = "countryName")
    var countryName: String = "",
    @ColumnInfo(name = "currencyValue")
    var currencyValue: Double = 0.0,
    @ColumnInfo(name = "updatedAt")
    var updatedAt: Long = 0L
){
    override fun toString(): String {
        return "$countryName - $currencyShortForm"
    }
}
