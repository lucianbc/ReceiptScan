package com.lucianbc.receiptscan.presentation.draft


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentReceiptBinding
import kotlinx.android.synthetic.main.receipt_layout.*


class ReceiptFragment :
    BaseFragment<DraftReviewViewModel>(DraftReviewViewModel::class.java) {

    private lateinit var itemsAdapter: ReceiptItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return setupBinding(inflater, container)?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observe(viewModel)
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentReceiptBinding? {
        val binding = DataBindingUtil.inflate<FragmentReceiptBinding>(
            inflater,
            R.layout.fragment_receipt,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding
    }

    private fun observe(viewModel: DraftReviewViewModel) {
        viewModel.receipt.observe(viewLifecycleOwner, Observer {
            itemsAdapter.submitList(it.products)
        })
    }

    private fun setupAdapter() {
        itemsAdapter = ReceiptItemsAdapter()
        receiptItemsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = itemsAdapter
        }
    }
}
