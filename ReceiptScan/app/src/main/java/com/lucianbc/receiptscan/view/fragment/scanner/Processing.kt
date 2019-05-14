package com.lucianbc.receiptscan.view.fragment.scanner


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentProcessingBinding
import com.lucianbc.receiptscan.viewmodel.scanner.ProcessingViewModel

class Processing:
    BaseFragment<ProcessingViewModel>(ProcessingViewModel::class.java) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentProcessingBinding>(
            inflater,
            R.layout.fragment_processing,
            container,
            false
        )

        initViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}
