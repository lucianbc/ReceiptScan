package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.presentation.MainActivity
import com.lucianbc.receiptscan.presentation.scanner.ScannerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeScannerActivity(): ScannerActivity
}