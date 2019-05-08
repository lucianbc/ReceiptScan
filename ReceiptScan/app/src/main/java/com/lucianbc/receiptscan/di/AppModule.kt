package com.lucianbc.receiptscan.di

import android.content.Context
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.ReceiptScan
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus

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
}



