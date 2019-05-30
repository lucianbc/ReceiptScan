package com.lucianbc.receiptscan.di

import android.content.Context
import com.lucianbc.receiptscan.presentation.ReceiptScan
import dagger.Module
import dagger.Provides


@Module
class AppModule {
    @Provides
    fun provideContext(application: ReceiptScan): Context =
        application.applicationContext
}