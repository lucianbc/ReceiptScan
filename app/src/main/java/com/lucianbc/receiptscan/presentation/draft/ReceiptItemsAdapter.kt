package com.lucianbc.receiptscan.presentation.draft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.Product
import kotlinx.android.synthetic.main.receipt_item_layout.view.*

class ReceiptItemsAdapter(private val onTap: (Product) -> Unit) : ListAdapter<Product, ReceiptItemViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.receipt_item_layout, parent, false)
        return ReceiptItemViewHolder(view, onTap)
    }

    override fun onBindViewHolder(holder: ReceiptItemViewHolder, position: Int) {
        val element = getItem(position)
        holder.product = element
    }

    class Diff : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldProduct: Product, newProduct: Product): Boolean {
            return oldProduct == newProduct
        }

        override fun areContentsTheSame(oldProduct: Product, newProduct: Product): Boolean {
            return oldProduct.name == newProduct.name && oldProduct.price == newProduct.price
        }
    }
}

class ReceiptItemViewHolder(val view: View, private val onTap: (Product) -> Unit) : RecyclerView.ViewHolder(view) {
    var product: Product? = null
        set(value) {
            field = value
            view.itemName.text = value?.name
            view.itemPrice.text = value?.price.toString()
            value?.let { view.setOnClickListener { onTap(value) } }
        }
}