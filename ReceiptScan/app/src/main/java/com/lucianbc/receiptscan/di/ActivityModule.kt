package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.view.activity.DraftReviewActivity
import com.lucianbc.receiptscan.view.activity.MainActivity
import com.lucianbc.receiptscan.view.activity.ScannerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeScannerActivity(): ScannerActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeDraftReviewActivity(): DraftReviewActivity
}
