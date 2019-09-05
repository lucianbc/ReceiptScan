package com.lucianbc.receiptscan.presentation.home.receipts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.home_currency_layout.view.*
import java.util.*

class CurrenciesAdapter : ListAdapter<Currency, CurrenciesAdapter.Holder>(Diff()) {
    fun getItemAt(position: Int): Currency? {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.home_currency_layout, parent, false)
            .let(::Holder)

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position).apply {
            holder.item = this
        }
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var item: Currency? = null
            set(value) {
                field = value
                view.currencyItemText.text = field?.currencyCode
            }
    }

    class Diff : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem.currencyCode == newItem.currencyCode

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem.currencyCode == newItem.currencyCode
    }
}