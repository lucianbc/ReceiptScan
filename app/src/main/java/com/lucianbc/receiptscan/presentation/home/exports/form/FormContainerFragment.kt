package com.lucianbc.receiptscan.presentation.home.exports.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentFormContainerBinding
import kotlinx.android.synthetic.main.fragment_form_container.*


class FormContainerFragment
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
    ): FragmentFormContainerBinding? {
        val binding = DataBindingUtil.inflate<FragmentFormContainerBinding>(
            inflater,
            R.layout.fragment_form_container,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = OptionsAdapter(fragmentManager!!)
        exportOptionsPager.adapter = adapter

        exportOptionsPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                viewModel.option.value =
                    (if (position == adapter.count- 1) FormViewModel.Option.Check else FormViewModel.Option.Next)
            }
        })

        wormDotsIndicator.setViewPager(exportOptionsPager)

        actionButton.setOnClickListener { viewModel.handleOption() }
    }
}
