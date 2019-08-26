package com.lucianbc.receiptscan.presentation.home.drafts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.drafts.DraftListItem
import kotlinx.android.synthetic.main.draft_item_layout.view.*

typealias DraftItemClick = ((DraftListItem) -> Unit)

class DraftsAdapter(private val itemCallback: DraftItemClick) : ListAdapter<DraftListItem, DraftItemViewHolder>(
    Diff()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.draft_item_layout, parent, false)

        return DraftItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: DraftItemViewHolder, position: Int) {
        val element = getItem(position)
        holder.item = element
        holder.view.setOnClickListener { itemCallback.invoke(element) }
    }

    class Diff : DiffUtil.ItemCallback<DraftListItem>() {
        override fun areContentsTheSame(oldItem: DraftListItem, newItem: DraftListItem): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: DraftListItem, newItem: DraftListItem): Boolean {
            return oldItem.creationTimestamp == newItem.creationTimestamp
        }
    }
}

class DraftItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var item: DraftListItem? = null
        set(value) {
            field = value
            view.draftItemTitle.text = "${value?.creationTimestamp}"
        }
}