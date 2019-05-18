package com.lucianbc.receiptscan.view.fragment.review

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.fragment.app.DialogFragment
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.lucianbc.receiptscan.domain.model.Tag
import kotlinx.android.parcel.Parcelize

class ScanInfoBoxDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_scaninfobox, container)

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )

        return view
    }

    companion object {
        private const val KEY = "args"

        fun create(args: Arguments): DialogFragment {
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
