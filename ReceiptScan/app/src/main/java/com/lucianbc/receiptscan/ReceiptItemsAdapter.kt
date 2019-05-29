package com.lucianbc.receiptscan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.domain.model.Receipt
import kotlinx.android.synthetic.main.receipt_item_layout.view.*

class ReceiptItemsAdapter : ListAdapter<Receipt.Item, ReceiptItemViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
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

class ReceiptItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var item: Receipt.Item? = null
        set(value) {
            field = value
            view.itemName.text = value?.name
            view.itemPrice.text = value?.price.toString()
        }
}