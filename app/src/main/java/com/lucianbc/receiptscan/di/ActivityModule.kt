package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.presentation.draft.DraftReviewActivity
import com.lucianbc.receiptscan.presentation.home.MainActivity
import com.lucianbc.receiptscan.presentation.home.exports.form.FormActivity
import com.lucianbc.receiptscan.presentation.receipt.ReceiptActivity
import com.lucianbc.receiptscan.presentation.scanner.ScannerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeScannerActivity(): ScannerActivity

    @ContributesAndroidInjector
    abstract fun contributeDraftReviewActivity(): DraftReviewActivity

    @ContributesAndroidInjector
    abstract fun contributeReceiptActivity(): ReceiptActivity

    @ContributesAndroidInjector
    abstract fun contributeFormActivity(): FormActivity
}