package com.lucianbc.receiptscan.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.google.firebase.storage.FirebaseStorage
import com.lucianbc.receiptscan.domain.model.ReceiptDefaults
import com.lucianbc.receiptscan.domain.model.SharingOption
import com.lucianbc.receiptscan.domain.viewfinder.LiveViewUseCase
import com.lucianbc.receiptscan.infrastructure.dao.AppDatabase
import com.lucianbc.receiptscan.infrastructure.dao.DraftDao
import com.lucianbc.receiptscan.infrastructure.dao.PreferencesDao
import com.lucianbc.receiptscan.presentation.ReceiptScan
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
    fun provideLiveViewUseCase(): LiveViewUseCase =
        LiveViewUseCase(5f)

    @Provides
    fun provideFirebaseRecognizer(): FirebaseVisionTextRecognizer =
        FirebaseVision.getInstance().onDeviceTextRecognizer

    @Provides
    fun eventBus(): EventBus =
        EventBus.getDefault()

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideDraftDao(database: AppDatabase): DraftDao =
        database.draftDao()

    @Provides
    @Singleton
    fun provideReceiptDefaults(dao: PreferencesDao) : ReceiptDefaults =
        dao

    @Provides
    @Singleton
    fun providePreferencesDao(context: Context) : PreferencesDao =
        PreferencesDao(context.getSharedPreferences("Preferences", Context.MODE_PRIVATE))

    @Provides
    @Singleton
    fun provideSharingOption(dao: PreferencesDao) : SharingOption =
        dao

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage =
        FirebaseStorage.getInstance()
}