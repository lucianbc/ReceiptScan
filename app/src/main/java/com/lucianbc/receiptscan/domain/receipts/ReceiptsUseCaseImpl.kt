package com.lucianbc.receiptscan.domain.receipts

import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReceiptsUseCaseImpl @Inject constructor(
    private val repository: ReceiptsRepository,
    private val manageFactory: ManageReceiptUseCase.Factory,
    private val sourcesManager: SourcesManager
) : ReceiptsUseCase, ISourcesManager by sourcesManager {

    override fun fetch(receiptId: ReceiptId): ReceiptsUseCase.Manage {
        return repository
            .getReceipt(receiptId)
            .subscribeOn(Schedulers.io())
            .let { manageFactory.create(it) }
    }
}