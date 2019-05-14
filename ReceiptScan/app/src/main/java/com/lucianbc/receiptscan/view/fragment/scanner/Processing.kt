package com.lucianbc.receiptscan.view.fragment.scanner


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.databinding.FragmentProcessingBinding
import com.lucianbc.receiptscan.viewmodel.scanner.ProcessingViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class Processing : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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

        val vm = ViewModelProviders
            .of(this, viewModelFactory)
            .get(ProcessingViewModel::class.java)

        binding.viewModel = vm
        binding.lifecycleOwner = this

        return binding.root
    }
}
