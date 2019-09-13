package com.lucianbc.receiptscan.presentation.home.exports

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.export.Export
import com.lucianbc.receiptscan.domain.export.Status
import com.lucianbc.receiptscan.domain.extract.rules.show
import kotlinx.android.synthetic.main.export_list_item_layout.view.*

class ExportsAdapter(private val activityStarter: (Intent) -> Unit)
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
                    if (value.status == Status.COMPLETE)
                        setupButtons(value, view)
                    else
                        hideButtons()
                }
            }

        private fun setupButtons(value: Export, view: View) {
            view.copyToClipboardBtn.visibility = View.VISIBLE
            view.downloadBtn.visibility = View.VISIBLE
            view.copyToClipboardBtn.setOnClickListener {
                val clipService = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("download url", value.downloadLink)
                clipService.primaryClip = clip
                Toast
                    .makeText(view.context, "Download link copied to clipboard", Toast.LENGTH_SHORT)
                    .show()
            }
            view.downloadBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(value.downloadLink))
                activityStarter(intent)
            }
        }

        private fun hideButtons() {
            view.copyToClipboardBtn.visibility = View.GONE
            view.downloadBtn.visibility = View.GONE
        }
    }
}