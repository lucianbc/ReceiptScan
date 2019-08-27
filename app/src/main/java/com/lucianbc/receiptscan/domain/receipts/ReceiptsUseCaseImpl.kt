package com.lucianbc.receiptscan.domain.receipts

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReceiptsUseCaseImpl @Inject constructor(
    private val repository: ReceiptsRepository,
    private val manageFactory: ManageReceiptUseCase.Factory
) : ReceiptsUseCase {
    override fun list(): Flowable<List<ReceiptListItem>> =
        repository.listReceipts()

    override fun fetch(receiptId: ReceiptId): ReceiptsUseCase.Manage {
        return repository
            .getReceipt(receiptId)
            .subscribeOn(Schedulers.io())
            .let { manageFactory.create(it) }
    }
}