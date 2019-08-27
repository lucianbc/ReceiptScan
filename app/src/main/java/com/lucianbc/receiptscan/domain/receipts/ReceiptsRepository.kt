package com.lucianbc.receiptscan.domain.receipts

import io.reactivex.Flowable

interface ReceiptsRepository {
    fun listReceipts(): Flowable<List<ReceiptListItem>>
    fun getReceipt(receiptId: ReceiptId): Flowable<Receipt>
}