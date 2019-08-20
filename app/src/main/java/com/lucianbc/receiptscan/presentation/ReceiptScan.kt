package com.lucianbc.receiptscan.presentation

import android.app.NotificationManager
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.lucianbc.receiptscan.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject
import com.lucianbc.receiptscan.presentation.home.exports.createNotificationChannel as createExportChannel

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

        getSystemService(NotificationManager::class.java).let(::createExportChannel)
    }
}