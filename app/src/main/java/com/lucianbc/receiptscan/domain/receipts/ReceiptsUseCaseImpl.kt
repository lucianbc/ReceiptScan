package com.lucianbc.receiptscan.domain.receipts

import io.reactivex.Flowable
import javax.inject.Inject

class ReceiptsUseCaseImpl @Inject constructor(
    private val receiptsRepository: ReceiptsRepository
) : ReceiptsUseCase {
    override fun list(): Flowable<List<ReceiptListItem>> =
        receiptsRepository.listReceipts()
}