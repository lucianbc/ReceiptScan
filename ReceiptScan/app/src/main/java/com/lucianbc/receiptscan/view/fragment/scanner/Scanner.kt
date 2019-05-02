package com.lucianbc.receiptscan.view.fragment.scanner


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.databinding.FragmentScannerBinding
import com.lucianbc.receiptscan.viewmodel.scanner.LiveViewVM
import com.otaliastudios.cameraview.Flash
import kotlinx.android.synthetic.main.fragment_scanner.*

class Scanner : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentScannerBinding>(
            inflater,
            R.layout.fragment_scanner,
            container,
            false
        )

        val vm = ViewModelProviders.of(this).get(LiveViewVM::class.java)
        binding.viewModel = vm
        binding.lifecycleOwner = this

        vm.flash.observe(this, Observer {
            when (it) {
                true -> scanner_view.flash = Flash.TORCH
                false -> scanner_view.flash = Flash.OFF
            }
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanner_view.setLifecycleOwner(viewLifecycleOwner)
    }
}
