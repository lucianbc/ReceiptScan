package com.lucianbc.receiptscan.di

import android.content.Context
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.domain.service.TaggingService
import com.lucianbc.receiptscan.domain.usecase.LiveViewUseCase
import com.lucianbc.receiptscan.presentation.ReceiptScan
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus


@Module
class AppModule {
    @Provides
    fun provideContext(application: ReceiptScan): Context =
        application.applicationContext

    @Provides
    fun provideLiveViewUseCase(): LiveViewUseCase =
        LiveViewUseCase(0.5f)

    @Provides
    fun provideFirebaseRecognizer(): FirebaseVisionTextRecognizer =
        FirebaseVision.getInstance().onDeviceTextRecognizer

    @Provides
    fun eventBus(): EventBus =
        EventBus.getDefault()
}