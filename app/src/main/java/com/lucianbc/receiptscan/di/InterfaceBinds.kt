package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.ReceiptSender
import com.lucianbc.receiptscan.infrastructure.repository.DraftsRepositoryImpl
import com.lucianbc.receiptscan.infrastructure.workers.ReceiptCollector
import dagger.Binds
import dagger.Module

@Module
abstract class InterfaceBinds {
    @Binds
    abstract fun bindDraftsRepo(obj: DraftsRepositoryImpl): DraftsRepository

    @Binds
    abstract fun bindReceiptSender(obj: ReceiptCollector.Runner): ReceiptSender
}