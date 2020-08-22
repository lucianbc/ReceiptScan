package com.lucianbc.receiptscan.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lucianbc.receiptscan.HomeFragmentDirections
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.home.dashboard.DashboardBottomSheetDialog
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dashboardFab.setOnClickListener {
            openBottomSheet()
        }
    }

    private fun openBottomSheet() =
        DashboardBottomSheetDialog(
            onSnap = {
                findNavController().navigate(HomeFragmentDirections.actionHomeToScanner())
            },
            onManuallyInput = {
                findNavController().navigate(HomeFragmentDirections.actionHomeToReceiptView())
            }
        ).show(childFragmentManager)
}