package com.paypay.paypaytest.presentation.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.paypay.paypaytest.R
import com.paypay.paypaytest.data.db.CurrencyDetailsModel
import com.paypay.paypaytest.databinding.ActivityCurrencyBinding
import com.paypay.paypaytest.presentation.viewmodel.CurrencyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyActivity : AppCompatActivity() {

    private lateinit var viewModel: CurrencyViewModel
    private lateinit var binding: ActivityCurrencyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_currency)
        viewModel = CurrencyViewModel(application)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.actvCurrencyName.threshold = 1
        viewModel.countryArray.observe(this, Observer {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, it
            )
            binding.actvCurrencyName.setAdapter(adapter)
        })
        viewModel.amount.observe(this) { amount ->
            if(!TextUtils.isEmpty(amount)){
                viewModel.selectedCountry.value?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.getListConvertedList(amount.toFloat(), it.currencyShortForm)
                    }
                }
            }
        }

        viewModel.database!!.currencyDao().getALLDetails().observe(this, Observer { list ->
            list?.let {
                viewModel.dbCurrencyList = it.sortedBy { details -> details.countryName }
                viewModel.countryArray.value = viewModel.dbCurrencyList?.toTypedArray()
            }

        })
        binding.actvCurrencyName.setOnItemClickListener { parent, view, position, id ->

            if (!TextUtils.isEmpty(viewModel.amount.value)) {
                viewModel.selectedCountry.value =
                    binding.actvCurrencyName.adapter.getItem(position) as CurrencyDetailsModel
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getListConvertedList(
                        viewModel.amount.value!!.toFloat(),
                        viewModel.selectedCountry.value!!.currencyShortForm
                    )
                }
            } else {
                Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
            }

        }
    }
}