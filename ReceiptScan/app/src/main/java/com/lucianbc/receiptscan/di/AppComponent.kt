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
        ActivityModule::class,
        FragmentModule::class,
        ViewModelBinds::class,
        ViewModelFactoryBinds::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent: AndroidInjector<ReceiptScan> {
    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<ReceiptScan>()
}