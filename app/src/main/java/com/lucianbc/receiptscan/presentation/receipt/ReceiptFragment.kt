package com.lucianbc.receiptscan.presentation.receipt


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
import kotlinx.android.synthetic.main.fragment_receipt.*

class ReceiptFragment
    : BaseFragment<ReceiptViewModel>(ReceiptViewModel::class.java) {

    private val itemsAdapter = ItemsAdapter()

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

    private fun setupBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentReceiptBinding? {
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

    private fun setupAdapter() {
        receiptItems.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = itemsAdapter
        }
    }

    private fun observe(viewModel: ReceiptViewModel) {
        viewModel.apply {
            products.observe(viewLifecycleOwner, Observer {
                it?.let { itemsAdapter.submitList(it) }
            })
        }
    }
}
