package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.domain.collect.CollectRepository
import com.lucianbc.receiptscan.domain.drafts.DraftsRepository
import com.lucianbc.receiptscan.domain.drafts.DraftsUseCase
import com.lucianbc.receiptscan.domain.drafts.DraftsUseCaseImpl
import com.lucianbc.receiptscan.domain.extract.ExtractRepository
import com.lucianbc.receiptscan.domain.receipts.ReceiptsRepository
import com.lucianbc.receiptscan.domain.receipts.ReceiptsUseCase
import com.lucianbc.receiptscan.domain.receipts.ReceiptsUseCaseImpl
import com.lucianbc.receiptscan.domain.export.ExportRepository
import com.lucianbc.receiptscan.domain.collect.ReceiptCollector
import com.lucianbc.receiptscan.infrastructure.repository.*
import com.lucianbc.receiptscan.infrastructure.workers.ReceiptSenderWorker
import dagger.Binds
import dagger.Module

@Module
abstract class InterfaceBinds {
    @Binds
    abstract fun bindDraftsRepo(obj: ExportRepositoryImpl): ExportRepository

    @Binds
    abstract fun bindReceiptSender(obj: ReceiptSenderWorker.Runner): ReceiptCollector

    @Binds
    abstract fun bindExtractRepo(obj: ExtractRepositoryImpl): ExtractRepository

    @Binds
    abstract fun bindDraftsUseCase(obj: DraftsUseCaseImpl): DraftsUseCase

    @Binds
    abstract fun bindDraftsRepository(obj: DraftsRepositoryImpl): DraftsRepository

    @Binds
    abstract fun bindReceiptsRepository(obj: ReceiptsRepositoryImpl): ReceiptsRepository

    @Binds
    abstract fun bindReceiptsUseCase(obj: ReceiptsUseCaseImpl): ReceiptsUseCase

    @Binds
    abstract fun bindCollectRepository(obj: CollectRepositoryImpl): CollectRepository
}