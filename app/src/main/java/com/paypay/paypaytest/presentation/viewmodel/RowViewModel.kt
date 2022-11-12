package com.paypay.paypaytest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.paypay.paypaytest.R

/*
* Use to display details in Row
* */
class RowViewModel(val color: Int, val name: String, val shortForm: String, val amount: String) :
    ViewModel() {
    var layoutID: Int = R.layout.row_converted_currency
}