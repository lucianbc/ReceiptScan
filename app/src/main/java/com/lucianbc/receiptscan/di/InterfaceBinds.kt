package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.infrastructure.repository.DraftsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class InterfaceBinds {
    @Binds
    abstract fun bindDraftsRepo(obj: DraftsRepositoryImpl): DraftsRepository
}