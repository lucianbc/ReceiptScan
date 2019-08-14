package com.lucianbc.receiptscan.di

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.presentation.draft.DraftViewModel
import com.lucianbc.receiptscan.presentation.home.drafts.DraftsViewModel
import com.lucianbc.receiptscan.presentation.home.receipts.ReceiptsViewModel
import com.lucianbc.receiptscan.presentation.home.settings.SettingsViewModel
import com.lucianbc.receiptscan.presentation.receipt.ReceiptViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(DraftsViewModel::class)
    abstract fun bindDraftsVm(vm: DraftsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReceiptsViewModel::class)
    abstract fun binReceiptsVm(vm: ReceiptsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DraftViewModel::class)
    abstract fun bindDraftVM(vm: DraftViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReceiptViewModel::class)
    abstract fun bindReceiptVM(vm: ReceiptViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsVM(vm: SettingsViewModel): ViewModel
}