package com.lucianbc.receiptscan.presentation.home.exports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.export.Export
import com.lucianbc.receiptscan.domain.extract.rules.show
import kotlinx.android.synthetic.main.export_list_item_layout.view.*

class ExportsAdapter
    : ListAdapter<Export, ExportsAdapter.Holder>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.export_list_item_layout, parent, false)
            .let(this::Holder)

    override fun onBindViewHolder(holder: Holder, position: Int) =
        getItem(position).let { holder.item = it }

    class Diff : DiffUtil.ItemCallback<Export>() {
        override fun areItemsTheSame(oldItem: Export, newItem: Export) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Export, newItem: Export) =
            oldItem == newItem
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        var item: Export? = null
            set(value) {
                field = value
                value?.let {
                    view.beginDateText.text = value.firstDate.show()
                    view.endDateText.text = value.lastDate.show()
                    view.statusText.text = value.status.name
                }
            }
    }
}