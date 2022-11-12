package com.paypay.paypaytest.data.db

import android.content.Context
import androidx.room.*
import com.paypay.paypaytest.domain.CurrencyDao


@Database(entities = [CurrencyDetailsModel::class], version = 1, exportSchema = true)
abstract class CurrencyDBDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        private var currencyDBDbInstance: CurrencyDBDatabase? = null
        private const val DB_NAME = "currency.db"

        fun getDataBase(context: Context): CurrencyDBDatabase? {
            if (currencyDBDbInstance == null) {

                synchronized(CurrencyDBDatabase::class) {
                    currencyDBDbInstance = Room.databaseBuilder(
                        context,
                        CurrencyDBDatabase::class.java, DB_NAME
                    ).allowMainThreadQueries().build()
                }
            }
            return currencyDBDbInstance
        }

        fun destroyDataBase() {
            currencyDBDbInstance = null
        }
    }
}