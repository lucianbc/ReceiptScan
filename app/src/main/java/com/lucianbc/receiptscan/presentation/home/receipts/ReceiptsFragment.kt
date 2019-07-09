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
import kotlinx.android.synthetic.main.fragment_receipts.*

class ReceiptsFragment :
    BaseFragment<ReceiptsViewModel>(ReceiptsViewModel::class.java) {

    private lateinit var receiptsAdapter: ReceiptsAdapter

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
        receiptsAdapter = ReceiptsAdapter()
        receiptsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = receiptsAdapter
        }
    }

    private fun observe(viewModel: ReceiptsViewModel) {
        viewModel.receipts.observe(viewLifecycleOwner, Observer {
            receiptsAdapter.submitList(it)
        })
    }
}
