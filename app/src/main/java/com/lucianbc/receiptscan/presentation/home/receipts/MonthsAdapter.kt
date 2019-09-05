package com.lucianbc.receiptscan.presentation.home.receipts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.home_month_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class MonthsAdapter : ListAdapter<Date, MonthsAdapter.Holder>(Diff()) {
    fun getItemAt(position: Int) =
        getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.home_month_layout, parent, false)
            .let(::Holder)

    override fun onBindViewHolder(holder: Holder, position: Int) =
        getItem(position).run {
            holder.item = this
        }

    private val monthFormat = SimpleDateFormat("MMMM", Locale.US)
    private val monthAndYearFormat = SimpleDateFormat("MMM yyyy", Locale.US)

    private val now = Date()

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var item: Date? = null
            set(value) {
                field = value
                view.monthItemText.text = value?.format()
            }
    }

    class Diff : DiffUtil.ItemCallback<Date>() {
        override fun areItemsTheSame(oldItem: Date, newItem: Date) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Date, newItem: Date) =
            oldItem == newItem
    }

    private fun Date.year() =
        Calendar.getInstance().let {
            it.time = this
            it.get(Calendar.YEAR)
        }

    private fun Date.format(): String? {
        return (
                if (this.year() == now.year())
                    monthFormat
                else
                    monthAndYearFormat
                ).format(this)
    }
}