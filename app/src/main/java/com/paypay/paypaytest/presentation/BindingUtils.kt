package com.paypay.paypaytest.presentation

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paypay.paypaytest.presentation.ui.RecyclerViewBindingAdapter
import com.paypay.paypaytest.presentation.viewmodel.RowViewModel

class BindingUtils {

    companion object {
        private const val ROW_DATA: String = "setRowData"
        private const val BACKGROUND: String = "setBackground"
        private const val VISIBILITY: String = "setVisibility"

        @JvmStatic
        @BindingAdapter(ROW_DATA)
        fun setRowLayoutData(recyclerView: RecyclerView, listData: List<RowViewModel>?) {
            if (listData == null) return
            var adapter = recyclerView.adapter
            if (adapter == null || listData.isEmpty()) {
                adapter = RecyclerViewBindingAdapter(listData)
                recyclerView.adapter = adapter
            } else {
                adapter.notifyDataSetChanged()
            }
        }

        @JvmStatic
        @BindingAdapter(BACKGROUND)
        fun setBackgroundColor(view: View, color: Int) {
            view.setBackgroundColor(ContextCompat.getColor(view.context, color))
        }

        @JvmStatic
        @BindingAdapter(VISIBILITY)
        fun setVisibility(view: View, visible: Boolean) {
            if(visible){
                view.visibility=View.VISIBLE
            }else{
                view.visibility=View.GONE
            }
        }
    }

}