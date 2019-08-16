package com.lucianbc.receiptscan.presentation

import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.lucianbc.receiptscan.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class ReceiptScan : DaggerApplication() {
    @Inject
    lateinit var workerFactory: WorkerFactory

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().create(this)

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        )
    }
}