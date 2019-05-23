package com.lucianbc.receiptscan.view.fragment.review

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.domain.model.Receipt
import kotlinx.android.synthetic.main.receipt_item_layout.view.*

class ReceiptItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var item: Receipt.Item? = null
        set(value) {
            field = value
            view.itemName.text = value?.name
            view.itemPrice.text = value?.price.toString()
        }
}