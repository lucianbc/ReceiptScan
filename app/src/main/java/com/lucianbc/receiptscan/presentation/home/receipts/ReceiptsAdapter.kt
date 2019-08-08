package com.lucianbc.receiptscan.presentation.home.receipts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import kotlinx.android.synthetic.main.receipt_list_item_layout.view.*

class ReceiptsAdapter(private val callback: (ListReceiptsUseCase.Item) -> Unit) :
    ListAdapter<ListReceiptsUseCase.Item, ReceiptsAdapter.Holder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.receipt_list_item_layout, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val element = getItem(position)
        holder.item = element
    }

    class Diff : DiffUtil.ItemCallback<ListReceiptsUseCase.Item>() {
        override fun areItemsTheSame(oldItem: ListReceiptsUseCase.Item, newItem: ListReceiptsUseCase.Item) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ListReceiptsUseCase.Item, newItem: ListReceiptsUseCase.Item): Boolean {
            return oldItem == newItem
        }
    }


    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var item: ListReceiptsUseCase.Item? = null
            set(value) {
                field = value
                value?.let {
                    view.receiptMerchant.text = value.merchantName
                    view.setOnClickListener { callback.invoke(value) }
                }
            }
    }
}