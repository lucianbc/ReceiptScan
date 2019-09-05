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
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_receipts.*

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
        setupViews()
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

    private fun setupViews() {
        currenciesCarousel.apply {
            initialize()
            onCurrencyChanged = viewModel::fetchForCurrency
        }
        monthsCarousel.apply{
            initialize()
            onMonthChanged = viewModel::fetchForMonth
        }
        categoriesCarousel.apply {
            initialize()
            onGroupChanged = viewModel::fetchForSpendingGroup
        }
    }

    private fun observe(viewModel: ReceiptsViewModel) {
        viewModel.receipts.observe(viewLifecycleOwner, Observer {
            receiptsAdapter.submitList(it)
        })
        viewModel.currencies.observe(viewLifecycleOwner, Observer {
            currenciesCarousel.submitList(it)
        })
        viewModel.months.observe(viewLifecycleOwner, Observer {
            monthsCarousel.submitList(it)
        })
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            categoriesCarousel.submitList(it)
        })
    }
}
