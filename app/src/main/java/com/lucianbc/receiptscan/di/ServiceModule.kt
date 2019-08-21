package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.presentation.home.exports.ExportService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun contributeExportService(): ExportService
}