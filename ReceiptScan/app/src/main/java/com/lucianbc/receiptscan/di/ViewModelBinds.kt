package com.lucianbc.receiptscan.di

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.viewmodel.scanner.LiveViewVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelBinds {
    @Binds
    @IntoMap
    @ViewModelKey(LiveViewVM::class)
    abstract fun bindVm(viewVM: LiveViewVM): ViewModel
}


