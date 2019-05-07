package com.lucianbc.receiptscan.di

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeScannerActivity(): com.lucianbc.receiptscan.view.activity.Scanner
}
