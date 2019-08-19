package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.presentation.draft.DraftFragment
import com.lucianbc.receiptscan.presentation.draft.ReceiptImageFragment
import com.lucianbc.receiptscan.presentation.home.drafts.DraftsFragment
import com.lucianbc.receiptscan.presentation.home.exports.ExportFragment
import com.lucianbc.receiptscan.presentation.home.exports.form.ContentFormatFragment
import com.lucianbc.receiptscan.presentation.home.exports.form.ContentFragment
import com.lucianbc.receiptscan.presentation.home.exports.form.DateRangeFragment
import com.lucianbc.receiptscan.presentation.home.exports.form.FormContainerFragment
import com.lucianbc.receiptscan.presentation.home.receipts.ReceiptsFragment
import com.lucianbc.receiptscan.presentation.home.settings.SettingsFragment
import com.lucianbc.receiptscan.presentation.receipt.ReceiptFragment
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
    abstract fun contributeAnnotationsFragment(): ReceiptImageFragment

    @ContributesAndroidInjector
    abstract fun contributeReceiptsFragment(): ReceiptsFragment

    @ContributesAndroidInjector
    abstract fun contributeDraftFragment(): DraftFragment

    @ContributesAndroidInjector
    abstract fun contributeReceiptFragment(): ReceiptFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeContentFragment(): ContentFragment

    @ContributesAndroidInjector
    abstract fun contributeContentFormatFragment(): ContentFormatFragment

    @ContributesAndroidInjector
    abstract fun contributeDateRangeFragment(): DateRangeFragment

    @ContributesAndroidInjector
    abstract fun contributeFormContainer(): FormContainerFragment

    @ContributesAndroidInjector
    abstract fun contributeExportFragment(): ExportFragment
}