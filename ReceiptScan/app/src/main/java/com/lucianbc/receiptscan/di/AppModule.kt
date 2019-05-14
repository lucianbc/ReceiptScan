package com.lucianbc.receiptscan.di

import android.content.Context
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.ReceiptScan
import com.lucianbc.receiptscan.domain.dao.AppDatabase
import com.lucianbc.receiptscan.domain.dao.ReceiptDraftDao
import com.lucianbc.receiptscan.domain.repository.DummyReceiptDraftRepo
import com.lucianbc.receiptscan.domain.repository.ReceiptDraftRepository
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideContext(application: ReceiptScan): Context =
        application.applicationContext

    @Provides
    fun eventBus(): EventBus =
        EventBus.getDefault()

    @Provides
    fun firebaseRecognizer(): FirebaseVisionTextRecognizer =
            FirebaseVision.getInstance().onDeviceTextRecognizer

    @Provides
    @Singleton
    fun appDatabase(context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun rdDao(database: AppDatabase): ReceiptDraftDao =
        database.receiptDraftDao()

    @Provides
    fun rdRepository(repo: DummyReceiptDraftRepo): ReceiptDraftRepository =
        repo
}



