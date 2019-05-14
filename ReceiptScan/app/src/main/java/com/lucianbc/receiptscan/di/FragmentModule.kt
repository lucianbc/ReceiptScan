package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.view.fragment.homepage.Drafts
import com.lucianbc.receiptscan.view.fragment.scanner.Processing
import com.lucianbc.receiptscan.view.fragment.scanner.Scanner
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeScanner(): Scanner

    @ContributesAndroidInjector
    abstract fun contributeDrafts(): Drafts

    @ContributesAndroidInjector
    abstract fun contributeProcessing(): Processing
}