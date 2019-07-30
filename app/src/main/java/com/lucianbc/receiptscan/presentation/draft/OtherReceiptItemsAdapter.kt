package com.lucianbc.receiptscan.presentation.draft

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.util.Callback
import kotlinx.android.synthetic.main.editable_item_layout.view.*

class OtherReceiptItemsAdapter : ListAdapter<Product, OtherReceiptItemsAdapter.Holder>(Diff()) {
    var onItemEdit: Callback<Product>? = null
    var onItemSwipe: Callback<Product>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.editable_item_layout, parent, false)
        return Holder(view, onItemEdit)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val element = getItem(position)
        holder.product = element
    }

    fun bindSwipe(recyclerView: RecyclerView) {
        ItemTouchHelper(
            object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val product = getItem(position)
                    onItemSwipe?.invoke(product)
                }
            }
        ).attachToRecyclerView(recyclerView)
    }

    class Holder(
        val view: View,
        private val onItemEdit: Callback<Product>?
    ): RecyclerView.ViewHolder(view) {
        private val editable = Editable.Factory.getInstance()
        var product: Product? = null
            set(value) {
                field = value
                view.itemName.text = value?.let { editable.newEditable(it.name) }
                view.itemPrice.text = value?.let { editable.newEditable(it.price.toString()) }
                view.itemName.addTextChangedListener {
                    value?.name = it.toString()
                    value?.let { t -> onItemEdit?.invoke(t) }
                }
                view.itemPrice.addTextChangedListener {
                    it.toString().toFloatOrNull()?.let { t -> value?.price = t }
                    value?.let { t -> onItemEdit?.invoke(t) }
                }
            }
    }

    class Diff : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
