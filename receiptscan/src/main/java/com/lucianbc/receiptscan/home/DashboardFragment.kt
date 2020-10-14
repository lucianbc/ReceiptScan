package com.lucianbc.receiptscan.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.HomeFragmentDirections
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.home.dashboard.DashboardBottomSheetDialog
import com.lucianbc.receiptscan.home.dashboard.Element
import com.lucianbc.receiptscan.home.dashboard.TransactionsAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*

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
        setupAdapter()
    }

    private fun setupAdapter() {
        val transactionsAdapter = TransactionsAdapter()
        transactionsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionsAdapter
        }
        transactionsAdapter.submitList(listOf(
            Element.Heading("group 1"),
            Element.Content("IC", "Title 1", "Subtitle 1", 33.398f, Currency.getInstance("EUR")),
            Element.Heading("group 2"),
            Element.Content("IC", "Title 2", "Subtitle 2", 33.3f, Currency.getInstance("GBP")),
            Element.Heading("group 3"),
            Element.Content("IC", "Title 3", "Subtitle 3", 33.3f, Currency.getInstance("EUR")),
            Element.Content("IC", "Title 4", "Subtitle 4", 33.3f, Currency.getInstance("RON")),
        ))
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