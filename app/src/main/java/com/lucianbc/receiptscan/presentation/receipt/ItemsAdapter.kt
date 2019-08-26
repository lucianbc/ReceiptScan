package com.lucianbc.receiptscan.presentation.receipt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.ProductEntity
import com.lucianbc.receiptscan.util.show
import kotlinx.android.synthetic.main.receipt_item_layout.view.*

class ItemsAdapter : ListAdapter<ProductEntity, ItemsAdapter.Holder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.receipt_item_layout, parent, false)
            .let { Holder(it) }

    override fun onBindViewHolder(holder: Holder, position: Int) =
        getItem(position)
            .let { holder.productEntity = it }


    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var productEntity: ProductEntity? = null
            set(value) {
                field = value
                view.itemName.text = value?.name
                view.itemPrice.text = value?.price.show()
            }
    }

    class Diff : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity) =
            oldItem == newItem
    }
}