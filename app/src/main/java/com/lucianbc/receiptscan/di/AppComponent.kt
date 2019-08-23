package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.presentation.ReceiptScan
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        WorkerBinds::class,
        ServiceModule::class,
        ActivityModule::class,
        InterfaceBinds::class,
        FragmentModule::class,
        ViewModelBinds::class,
        ViewModelFactoryBinds::class,
        AndroidSupportInjectionModule::class,
        AssistedModule::class
    ]
)
interface AppComponent : AndroidInjector<ReceiptScan> {
    @Suppress("DEPRECATION")
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ReceiptScan>()
}