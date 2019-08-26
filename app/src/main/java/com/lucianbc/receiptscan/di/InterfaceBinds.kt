package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.domain.drafts.DraftsRepository
import com.lucianbc.receiptscan.domain.drafts.DraftsUseCase
import com.lucianbc.receiptscan.domain.drafts.DraftsUseCaseImpl
import com.lucianbc.receiptscan.domain.extract.ExtractRepository
import com.lucianbc.receiptscan.domain.repository.AppRepository
import com.lucianbc.receiptscan.domain.service.ReceiptSender
import com.lucianbc.receiptscan.infrastructure.repository.AppRepositoryImpl
import com.lucianbc.receiptscan.infrastructure.repository.DraftsRepositoryImpl
import com.lucianbc.receiptscan.infrastructure.repository.OtherRepository
import com.lucianbc.receiptscan.infrastructure.workers.ReceiptSenderWorker
import dagger.Binds
import dagger.Module

@Module
abstract class InterfaceBinds {
    @Binds
    abstract fun bindDraftsRepo(obj: AppRepositoryImpl): AppRepository

    @Binds
    abstract fun bindReceiptSender(obj: ReceiptSenderWorker.Runner): ReceiptSender

    @Binds
    abstract fun bindExtractRepo(obj: OtherRepository): ExtractRepository

    @Binds
    abstract fun bindDraftsUseCase(obj: DraftsUseCaseImpl): DraftsUseCase

    @Binds
    abstract fun bindDraftsRepository(obj: DraftsRepositoryImpl): DraftsRepository
}