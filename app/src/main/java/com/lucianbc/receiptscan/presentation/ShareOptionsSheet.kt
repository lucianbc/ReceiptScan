package com.lucianbc.receiptscan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.share_options_layout.*

class ShareOptionsSheet : BottomSheetDialogFragment() {
    var onTextOnly: (() -> Unit)? = null
    var onImageOnly: (() -> Unit)? = null
    var onBoth: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.share_options_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textOnlyOption.setOnClickListener {
            onTextOnly?.invoke()
            dismiss()
        }
        imageOnlyOption.setOnClickListener {
            onImageOnly?.invoke()
            dismiss()
        }
        bothOption.setOnClickListener {
            onBoth?.invoke()
            dismiss()
        }
    }

    companion object {
        private const val TAG = "SHARE_OPTIONS_FORM"

        fun show(fragmentManager: FragmentManager) {
            val sheet = ShareOptionsSheet()
            sheet.show(fragmentManager, TAG)
        }
    }
}