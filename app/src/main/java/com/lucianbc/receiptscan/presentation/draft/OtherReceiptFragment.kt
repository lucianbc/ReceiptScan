package com.lucianbc.receiptscan.presentation.draft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.databinding.FragmentReceipt2Binding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OtherReceiptFragment : DaggerFragment() {

    private lateinit var itemsAdapter: OtherReceiptItemsAdapter

    @Inject
    lateinit var viewModelFactory: DraftReviewViewModel.Factory
    lateinit var viewModel: DraftReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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

    private fun setupBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentReceipt2Binding? {
        val binding = DataBindingUtil.inflate<FragmentReceipt2Binding>(
            inflater,
            R.layout.fragment_receipt_2,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding
    }
}