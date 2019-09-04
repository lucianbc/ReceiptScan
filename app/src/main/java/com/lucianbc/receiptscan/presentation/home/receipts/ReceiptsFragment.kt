package com.lucianbc.receiptscan.presentation.home.receipts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentReceiptsBinding
import com.lucianbc.receiptscan.presentation.receipt.ReceiptActivity
import kotlinx.android.synthetic.main.fragment_receipts.*
import java.util.*

class ReceiptsFragment :
    BaseFragment<ReceiptsViewModel>(ReceiptsViewModel::class.java) {

    private val receiptsAdapter = ReceiptsAdapter {
        ReceiptActivity.navIntent(activity!!, it.id).apply(::startActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        return setupBinding(inflater, container)?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setup()
        observe(viewModel)
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentReceiptsBinding? {
        val binding = DataBindingUtil.inflate<FragmentReceiptsBinding>(
            inflater,
            R.layout.fragment_receipts,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    private fun setupAdapter() {
        receiptsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = receiptsAdapter
        }
    }

    private fun setup() {
        currenciesCarousel.initialize(CurrenciesAdapter().apply {
            submitList(
                listOf("RON", "EUR", "GBP", "USD")
                    .map { Currency.getInstance(it) }
            )
        })
    }

    private fun observe(viewModel: ReceiptsViewModel) {
        viewModel.receipts.observe(viewLifecycleOwner, Observer {
            receiptsAdapter.submitList(it)
        })
    }
}
