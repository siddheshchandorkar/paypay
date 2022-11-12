package com.paypay.paypaytest.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paypay.paypaytest.BR
import com.paypay.paypaytest.databinding.RowConvertedCurrencyBinding
import com.paypay.paypaytest.presentation.viewmodel.RowViewModel


class RecyclerViewBindingAdapter(private var data: List<RowViewModel>) :
    ListAdapter<RowViewModel, RecyclerViewBindingAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowConvertedCurrencyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            viewType, parent, false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: RowConvertedCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RowViewModel) {
            binding.setVariable(BR.vm, item)
            binding.executePendingBindings()
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].layoutID
    }

    fun setData(items: ArrayList<RowViewModel>) {
        data = items
        notifyDataSetChanged()
    }


    object diffCallback : DiffUtil.ItemCallback<RowViewModel>() {
        override fun areItemsTheSame(oldItem: RowViewModel, newItem: RowViewModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RowViewModel, newItem: RowViewModel): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
}
