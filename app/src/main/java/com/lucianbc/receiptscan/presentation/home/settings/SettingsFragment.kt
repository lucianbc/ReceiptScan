package com.lucianbc.receiptscan.presentation.home.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.base.BaseFragment
import com.lucianbc.receiptscan.databinding.FragmentSettingsBinding
import com.lucianbc.receiptscan.presentation.Event
import kotlinx.android.synthetic.main.fragment_settings.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class SettingsFragment
    : BaseFragment<SettingsViewModel>(SettingsViewModel::class.java) {

    @Inject
    lateinit var eventBus: EventBus

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
        sendReceiptForm.setOnClickListener {
            switch1.toggle()
        }
    }

    private fun setupBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentSettingsBinding? =
        DataBindingUtil.inflate<FragmentSettingsBinding>(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        ).apply {
            viewModel = this@SettingsFragment.viewModel
            lifecycleOwner = this@SettingsFragment
        }

    private fun setupActions() {
        currencyForm.setOnClickListener { eventBus.post(Event.CurrencyTapped) }
        categoryForm.setOnClickListener { eventBus.post(Event.CategoryTapped) }
    }
}

