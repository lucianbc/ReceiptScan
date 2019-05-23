package com.lucianbc.receiptscan.view.fragment.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.Receipt

class ItemsAdapter: ListAdapter<Receipt.Item, ReceiptItemViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.receipt_item_layout, parent, false)
        return ReceiptItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceiptItemViewHolder, position: Int) {
        val element = getItem(position)
        holder.item = element
    }

    class Diff: DiffUtil.ItemCallback<Receipt.Item>() {
        override fun areItemsTheSame(oldItem: Receipt.Item, newItem: Receipt.Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Receipt.Item, newItem: Receipt.Item): Boolean {
            return oldItem.name == newItem.name && oldItem.price == newItem.price
        }

    }
}
