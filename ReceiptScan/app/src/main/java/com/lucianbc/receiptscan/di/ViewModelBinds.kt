package com.lucianbc.receiptscan.di

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.presentation.scanner.ProcessingViewModel
import com.lucianbc.receiptscan.presentation.scanner.ScannerViewModel
import com.lucianbc.receiptscan.presentation.scanner.ViewfinderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBinds {
    @Binds
    @IntoMap
    @ViewModelKey(ViewfinderViewModel::class)
    abstract fun bindViewfinderVm(vm: ViewfinderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScannerViewModel::class)
    abstract fun bindScannerVm(vm: ScannerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProcessingViewModel::class)
    abstract fun bindProcessingVm(vm: ProcessingViewModel): ViewModel
}