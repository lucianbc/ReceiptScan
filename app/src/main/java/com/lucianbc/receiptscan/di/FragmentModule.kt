package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.presentation.draft.OtherReceiptFragment
import com.lucianbc.receiptscan.presentation.draft.ReceiptFragment
import com.lucianbc.receiptscan.presentation.draft.ReceiptImageFragment
import com.lucianbc.receiptscan.presentation.home.drafts.DraftsFragment
import com.lucianbc.receiptscan.presentation.home.receipts.ReceiptsFragment
import com.lucianbc.receiptscan.presentation.scanner.ProcessingFragment
import com.lucianbc.receiptscan.presentation.scanner.ViewfinderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeViewfinderFragment(): ViewfinderFragment

    @ContributesAndroidInjector
    abstract fun contributeProcessingFragment(): ProcessingFragment

    @ContributesAndroidInjector
    abstract fun contributeDraftsFragment(): DraftsFragment

    @ContributesAndroidInjector
    abstract fun contributeDraftReceiptFragment(): ReceiptFragment

    @ContributesAndroidInjector
    abstract fun contributeAnnotationsFragment(): ReceiptImageFragment

    @ContributesAndroidInjector
    abstract fun contributeReceiptsFragment(): ReceiptsFragment

    @ContributesAndroidInjector
    abstract fun contributeOtherReceiptFragment(): OtherReceiptFragment
}