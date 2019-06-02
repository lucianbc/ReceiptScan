package com.lucianbc.receiptscan.presentation.home.drafts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.DraftItem
import kotlinx.android.synthetic.main.draft_item_layout.view.*


typealias DraftItemClick = ((DraftItem) -> Unit)

class DraftsAdapter(private val itemCallback: DraftItemClick) : ListAdapter<DraftItem, DraftItemViewHolder>(
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

    class Diff : DiffUtil.ItemCallback<DraftItem>() {
        override fun areContentsTheSame(oldItem: DraftItem, newItem: DraftItem): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: DraftItem, newItem: DraftItem): Boolean {
            return oldItem.creationTimestamp == newItem.creationTimestamp
        }
    }
}

class DraftItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var item: DraftItem? = null
        set(value) {
            field = value
            view.draftItemTitle.text = "${value?.creationTimestamp}"
        }
}