package com.lucianbc.receiptscan.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucianbc.receiptscan.ReceiptScan
import com.lucianbc.receiptscan.view.fragment.scanner.Scanner
import com.lucianbc.receiptscan.viewmodel.scanner.LiveViewVM
import dagger.*
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
class AppModule {
    @Provides
    fun provideContext(application: ReceiptScan): Context = application.applicationContext

    @Provides
    fun eventBus(): EventBus = EventBus.getDefault()
}



