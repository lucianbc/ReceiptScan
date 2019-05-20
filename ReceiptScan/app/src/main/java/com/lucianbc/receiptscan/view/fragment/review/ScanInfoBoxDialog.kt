package com.lucianbc.receiptscan.view.fragment.review

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.lucianbc.receiptscan.domain.model.Tag
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_scaninfobox.*

class ScanInfoBoxDialog : DialogFragment() {
    var callback: ((Arguments) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_scaninfobox, container)
        setupWindow()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupContent()
    }

    private fun setupContent() {
        arguments?.apply {
            val args = this.getParcelable<Arguments>(KEY)
            info_box_tag.adapter = ArrayAdapter<Tag>(
                context!!,
                android.R.layout.simple_spinner_item,
                Tag.values()
            )
            cancel_btn.setOnClickListener { dismiss() }
            args?.apply {
                info_box_text.setText(this.text)
                info_box_tag.setSelection(this.tag.ordinal, false)
                ok_btn.setOnClickListener { callback?.invoke(fromUi(this)); dismiss() }
            }
        }
    }

    private fun fromUi(args: Arguments): Arguments {
        @Suppress("UNCHECKED_CAST")
        val tag: Tag = (info_box_tag.adapter as ArrayAdapter<Tag>).getItem(info_box_tag.selectedItemPosition)!!
        return Arguments(args.id, info_box_text.text.toString(), tag)
    }

    private fun setupWindow() {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
    }


    companion object {
        private const val KEY = "args"

        fun create(args: Arguments): ScanInfoBoxDialog {
            val frag = ScanInfoBoxDialog()
            val bundle = Bundle()
            bundle.putParcelable(KEY, args)
            frag.arguments = bundle
            return frag
        }
    }

    @Parcelize
    data class Arguments (
        val id      : ID,
        val text    : String,
        val tag     : Tag
    ): Parcelable {
        companion object {
            fun from(infoBox: ScanInfoBox): Arguments {
                return Arguments(infoBox.id, infoBox.text, infoBox.tag)
            }
        }
    }
}
