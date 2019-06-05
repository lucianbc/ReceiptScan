package com.lucianbc.receiptscan.presentation.draft


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.databinding.FragmentReceiptBinding
import com.lucianbc.receiptscan.domain.service.show
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.receipt_layout.*
import javax.inject.Inject


class ReceiptFragment : DaggerFragment() {

    private lateinit var itemsAdapter: ReceiptItemsAdapter

    @Inject
    lateinit var viewModelFactory: DraftReviewViewModel.Factory
    lateinit var viewModel: DraftReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return setupBinding(inflater, container)?.root
    }

    private fun initParentViewModel() {
        viewModel = ViewModelProviders
            .of(activity!!, viewModelFactory)
            .get(DraftReviewViewModel::class.java)
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
            merchantTextView.text = it.receipt.merchantName
            dateTextView.text = it.receipt.date?.show()
            currencyTextView.text = it.receipt.currency?.toString()
            totalTextView.text = it.receipt.total?.toString()
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
