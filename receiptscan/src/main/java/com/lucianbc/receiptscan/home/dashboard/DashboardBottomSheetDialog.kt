package com.lucianbc.receiptscan.home.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lucianbc.receiptscan.R

class DashboardBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dashboard_bottom_sheet, container, false)
    }

    fun show(fm: FragmentManager) {
        this.show(fm, TAG)
    }

    companion object {
        private const val TAG = "DASHBOARD_BOTTOM_SHEET"
    }
}