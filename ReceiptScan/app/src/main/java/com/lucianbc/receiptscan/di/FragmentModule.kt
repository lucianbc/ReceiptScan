package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.view.fragment.scanner.Scanner
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeScanner(): Scanner
}