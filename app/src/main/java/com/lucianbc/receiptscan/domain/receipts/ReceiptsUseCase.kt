package com.lucianbc.receiptscan.domain.receipts

import io.reactivex.Flowable

interface ReceiptsUseCase {
    fun list(): Flowable<List<ReceiptListItem>>
}