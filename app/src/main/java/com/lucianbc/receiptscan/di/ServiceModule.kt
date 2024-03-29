package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.infrastructure.services.NotificationService
import com.lucianbc.receiptscan.infrastructure.services.ExportService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun contributeExportService(): ExportService

    @ContributesAndroidInjector
    abstract fun contributeNotificationService(): NotificationService
}