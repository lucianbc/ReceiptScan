package com.lucianbc.receiptscan.view.fragment.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import kotlinx.android.synthetic.main.draft_list_item.view.*

class DraftsListAdapter(
    private val clickListener: (ReceiptDraft) -> Unit
): ListAdapter<ReceiptDraft, DraftsListAdapter.DraftViewHolder>(ReceiptDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.draft_list_item, parent, false)
        return DraftViewHolder(view)
    }

    override fun onBindViewHolder(holder: DraftViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)
    }

    inner class DraftViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var posTbox: TextView = view.item_pos
        private val contTbox: TextView = view.item_content
        fun bind(element: ReceiptDraft, listener: (ReceiptDraft) -> Unit, position: Int) {
            posTbox.text = position.toString()
            contTbox.text = "${element.id.toString()} - ${element.imagePath}"
            this.itemView.setOnClickListener { listener(element) }
        }
    }
}

class ReceiptDiffCallback: DiffUtil.ItemCallback<ReceiptDraft>() {
    override fun areItemsTheSame(oldItem: ReceiptDraft, newItem: ReceiptDraft) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ReceiptDraft, newItem: ReceiptDraft) =
        oldItem.imagePath == newItem.imagePath
}