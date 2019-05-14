package com.lucianbc.receiptscan.view.fragment.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.ReceiptDraft

class DraftsListAdapter (
    private val clickCallback: (ReceiptDraft) -> Unit
) : ListAdapter<ReceiptDraft, DraftsListAdapter.DraftViewHolder>(ReceiptDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.draft_list_item, parent, false)
        return DraftViewHolder(view)
    }

    override fun onBindViewHolder(holder: DraftViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DraftViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(element: ReceiptDraft) {
            this.itemView.setOnClickListener {
                clickCallback(element)
            }
        }
    }
}

class ReceiptDiffCallback: DiffUtil.ItemCallback<ReceiptDraft>() {
    override fun areItemsTheSame(oldItem: ReceiptDraft, newItem: ReceiptDraft) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ReceiptDraft, newItem: ReceiptDraft) =
        oldItem.imagePath == newItem.imagePath
}