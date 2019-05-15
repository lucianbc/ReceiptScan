package com.lucianbc.receiptscan.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<VM: ViewModel> (
    private val vm: Class<VM>
): DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var viewModel: VM

    protected fun initViewModel() {
        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(vm)
    }

    protected fun initParentViewModel() {
        viewModel = ViewModelProviders
            .of(activity!!, viewModelFactory)
            .get(vm)
    }
}