package com.lucianbc.receiptscan.home.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lucianbc.receiptscan.R
import kotlinx.android.synthetic.main.dashboard_bottom_sheet.*

private typealias Callback = () -> Unit

class DashboardBottomSheetDialog(private val onSnap: Callback?, private val onManuallyInput: Callback?) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dashboard_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        snapInputBtn.setOnClickListener {
            dismiss()
            onSnap?.invoke()
        }
        manuallyInputBtn.setOnClickListener {
            dismiss()
            onManuallyInput?.invoke()
        }
    }

    fun show(fm: FragmentManager) {
        this.show(fm, TAG)
    }

    companion object {
        private const val TAG = "DASHBOARD_BOTTOM_SHEET"
    }
}