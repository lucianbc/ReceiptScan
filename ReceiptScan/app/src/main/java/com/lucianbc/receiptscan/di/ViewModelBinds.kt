@file:Suppress("unused")

package com.lucianbc.receiptscan.di

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.viewmodel.DraftReviewViewModel
import com.lucianbc.receiptscan.viewmodel.DraftsViewModel
import com.lucianbc.receiptscan.viewmodel.MainViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(DraftReviewViewModel::class)
    abstract fun bindDraftReviewViewModel(draftReviewVM: DraftReviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
}


