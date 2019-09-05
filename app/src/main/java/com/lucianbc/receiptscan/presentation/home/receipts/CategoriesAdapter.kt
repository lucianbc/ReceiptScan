package com.lucianbc.receiptscan.presentation.home.receipts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.receipts.SpendingGroup
import com.lucianbc.receiptscan.presentation.displayName
import com.lucianbc.receiptscan.presentation.icon
import com.lucianbc.receiptscan.util.show
import kotlinx.android.synthetic.main.home_category_layout.view.*

class CategoriesAdapter : ListAdapter<SpendingGroup, CategoriesAdapter.Holder>(Diff()) {
    fun getItemAt(position: Int) =
        getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.home_category_layout, parent, false)
            .let(::Holder)

    override fun onBindViewHolder(holder: Holder, position: Int) =
        getItem(position).run {
            holder.item = this
        }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var item: SpendingGroup? = null
            set(value) {
                field = value
                value?.let {
                    view.categoryItemImage.setImageResource(it.icon)
                    view.categoryItemName.text = it.displayName
                    view.categoryItemCurrency.text = it.currency.show()
                    view.categoryItemSum.text = it.total.show()
                }
            }
    }

    class Diff : DiffUtil.ItemCallback<SpendingGroup>() {
        override fun areItemsTheSame(oldItem: SpendingGroup, newItem: SpendingGroup): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: SpendingGroup, newItem: SpendingGroup): Boolean =
            oldItem == newItem
    }
}