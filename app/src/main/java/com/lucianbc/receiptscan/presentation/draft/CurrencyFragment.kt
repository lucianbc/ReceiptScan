package com.lucianbc.receiptscan.presentation.draft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import kotlinx.android.synthetic.main.currency_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_currency.*
import java.util.*

class CurrencyFragment
    : BaseFragment<DraftViewModel>(DraftViewModel::class.java) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParentViewModel()
        currenciesList.apply {
            adapter = CurrencyAdapter {
                viewModel.updateCurrency(it)
                fragmentManager?.popBackStack()
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }
}

private class CurrencyAdapter(private val callback: ((Currency) -> Unit))
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

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var item : Currency? = null
            set(value) {
                field = value
                view.currencyCode.text = value?.currencyCode
                view.currencyName.text = value?.displayName
                view.setOnClickListener { value?.let { callback.invoke(value) } }
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