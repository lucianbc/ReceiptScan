package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.domain.extract.ExtractRepository
import com.lucianbc.receiptscan.domain.extract.ExtractUseCase
import com.lucianbc.receiptscan.domain.extract.ExtractUseCaseImpl
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.ReceiptSender
import com.lucianbc.receiptscan.infrastructure.repository.DraftsRepositoryImpl
import com.lucianbc.receiptscan.infrastructure.repository.OtherRepository
import com.lucianbc.receiptscan.infrastructure.workers.ReceiptSenderWorker
import dagger.Binds
import dagger.Module

@Module
abstract class InterfaceBinds {
    @Binds
    abstract fun bindDraftsRepo(obj: DraftsRepositoryImpl): DraftsRepository

    @Binds
    abstract fun bindReceiptSender(obj: ReceiptSenderWorker.Runner): ReceiptSender

    @Binds
    abstract fun bindExtractRepo(obj: OtherRepository): ExtractRepository

    @Binds
    abstract fun bindExtractUseCase(obj: ExtractUseCaseImpl): ExtractUseCase
}