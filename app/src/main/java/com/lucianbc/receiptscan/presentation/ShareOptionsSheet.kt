package com.lucianbc.receiptscan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lucianbc.receiptscan.R

class ShareOptionsSheet : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.share_options_layout, container, false)
    }

    companion object {
        private const val TAG = "SHARE_OPTIONS_FORM"

        fun show(fragmentManager: FragmentManager) {
            val sheet = ShareOptionsSheet()
            sheet.show(fragmentManager, TAG)
        }
    }
}