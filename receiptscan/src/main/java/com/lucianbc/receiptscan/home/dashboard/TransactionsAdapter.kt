package com.lucianbc.receiptscan.home.dashboard


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.transaction_heading_layout.view.*
import kotlinx.android.synthetic.main.transaction_item_layout.view.*
import java.util.*

typealias Icon = String

sealed class Element {
    data class Heading(val value: String) : Element()
    data class Content(
        val icon: Icon,
        val title: String,
        val subtitle: String,
        val price: Float,
        val currency: Currency
    ) : Element()
}

class TransactionsAdapter : ListAdapter<Element, TransactionViewHolder>(
    Diff()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layout = when(viewType) {
            HEADING -> R.layout.transaction_heading_layout
            else -> R.layout.transaction_item_layout
        }
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val element = getItem(position)
        holder.setItem(element)
    }

    override fun getItemViewType(position: Int) = when(getItem(position)) {
        is Element.Heading -> HEADING
        is Element.Content -> CONTENT
    }

    class Diff : DiffUtil.ItemCallback<Element>() {
        override fun areItemsTheSame(oldItem: Element, newItem: Element): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Element, newItem: Element): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val HEADING = 1
        private const val CONTENT = 2
    }
}

class TransactionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun setItem(item: Element) {
        when(item) {
            is Element.Heading -> view.heading.text = item.value
            is Element.Content -> {
                view.icon.text = item.icon
                view.title.text = item.title
                view.subtitle.text = item.subtitle
                view.currency.text = view.context.getString(
                    R.string.price_template,
                    item.currency.symbol,
                    item.price,
                )
            }
        }
    }
}

fun Float.format(precision: Int): String {
    return "%.2f".format(this)
}
