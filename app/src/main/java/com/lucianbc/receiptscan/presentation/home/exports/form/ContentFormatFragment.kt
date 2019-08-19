package com.lucianbc.receiptscan.presentation.home.exports.form


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentContentFormatBinding


class ContentFormatFragment
    : BaseFragment<FormViewModel>(FormViewModel::class.java) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParentViewModel()
        return setupBinding(inflater, container)?.root
    }

    private fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContentFormatBinding? {
        val binding = DataBindingUtil.inflate<FragmentContentFormatBinding>(
            inflater,
            R.layout.fragment_content_format,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding
    }
}
