package com.lucianbc.receiptscan.presentation.draft

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.activity_currency.*
import kotlinx.android.synthetic.main.currency_item_layout.view.*
import java.util.*

class CurrencyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)
        setSupportActionBar(toolbar)
        currenciesList.apply {
            adapter = CurrencyAdapter()
            layoutManager = LinearLayoutManager(this@CurrencyActivity)
        }
    }

    companion object {
        fun navIntent(context: Context) =
            Intent(context, CurrencyActivity::class.java)
    }
}

private class CurrencyAdapter
    : ListAdapter<Currency, CurrencyAdapter.Holder>(Diff()) {

    init {
        submitList(Currency.getAvailableCurrencies().toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.currency_item_layout, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val element = getItem(position)
        holder.item = element
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var item : Currency? = null
            set(value) {
                field = value
                view.currencyCode.text = value?.currencyCode
                view.currencyName.text = value?.displayName
            }
    }

    class Diff : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.currencyCode == newItem.currencyCode
        }
    }
}