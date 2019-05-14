package com.lucianbc.receiptscan.di

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.viewmodel.DraftsViewModel
import com.lucianbc.receiptscan.viewmodel.scanner.LiveViewVM
import com.lucianbc.receiptscan.viewmodel.scanner.ProcessingViewModel
import com.lucianbc.receiptscan.viewmodel.scanner.ScannerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelBinds {
    @Binds
    @IntoMap
    @ViewModelKey(ScannerViewModel::class)
    abstract fun bindScannerVm(viewVM: ScannerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DraftsViewModel::class)
    abstract fun bindDraftVm(draftsVM: DraftsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LiveViewVM::class)
    abstract fun bindLiveViewVM(liveViewVM: LiveViewVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProcessingViewModel::class)
    abstract fun bindProcessingVm(processingVM: ProcessingViewModel): ViewModel
}


