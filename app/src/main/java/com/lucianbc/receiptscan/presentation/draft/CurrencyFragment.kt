package com.lucianbc.receiptscan.presentation.draft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.currency_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_currency.*
import java.util.*

class CurrencyFragment(
    private val callback: (Currency) -> Unit
) : Fragment() {

    private lateinit var currenciesAdapter: CurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currenciesAdapter = CurrencyAdapter {
            callback.invoke(it)
            fragmentManager?.popBackStack()
        }
        currenciesList.apply {
            adapter = currenciesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        (toolbar.menu.getItem(0).actionView as SearchView).apply(::initSearch)
    }

    private fun initSearch(view: SearchView) {
        view.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currenciesAdapter.filter.filter(newText)
                return false
            }
        })
    }
}

private class CurrencyAdapter(private val callback: ((Currency) -> Unit))
    : ListAdapter<Currency, CurrencyAdapter.Holder>(Diff()), Filterable {

    private var allItems = emptyList<Currency>()

    init {
        submitList(Currency.getAvailableCurrencies().toList())
    }

    override fun submitList(list: List<Currency>?) {
        list?.let { allItems = list }
        super.submitList(list)
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

    private fun submitWithoutSave(list: List<Currency>?) = super.submitList(list)

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return FilterResults().apply { values = filterCurrencies(constraint, allItems) }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            submitWithoutSave(results?.values as List<Currency>?)
        }
    }
}

private fun filterCurrencies(constraint: CharSequence?, items: List<Currency>): List<Currency> {
    return if (constraint == null || constraint.isEmpty())
        items
    else {
        val const = constraint.toString().toLowerCase().trim()
        items.filter { currency -> currency.currencyCode.toLowerCase().contains(const) }
    }
}