package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.presentation.scanner.ProcessingFragment
import com.lucianbc.receiptscan.presentation.scanner.ViewfinderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeViewfinderFragment(): ViewfinderFragment

    @ContributesAndroidInjector
    abstract fun contributeProcessingFragment(): ProcessingFragment
}