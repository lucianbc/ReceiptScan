package com.lucianbc.receiptscan.presentation.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentProcessingBinding

class ProcessingFragment :
    BaseFragment<ProcessingViewModel>(ProcessingViewModel::class.java) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        return setupBinding(inflater, container)?.root
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProcessingBinding? {
        val binding = DataBindingUtil.inflate<FragmentProcessingBinding>(
            inflater,
            R.layout.fragment_processing,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }
}
