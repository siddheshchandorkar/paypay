package com.paypay.paypaytest.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import com.paypay.paypaytest.data.db.CurrencyDetailsModel

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM `currency_details`")
    fun getALLDetails(): LiveData<List<CurrencyDetailsModel>>

    @Query("SELECT * FROM `currency_details` WHERE currencyShortForm= :shortForm")
    fun getCurrencyByShortForm(shortForm: String): CurrencyDetailsModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertList(currency: ArrayList<CurrencyDetailsModel>): List<Long>

    @Update
    fun update(currency: CurrencyDetailsModel)

}