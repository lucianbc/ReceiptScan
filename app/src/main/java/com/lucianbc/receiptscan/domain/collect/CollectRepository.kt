package com.lucianbc.receiptscan.domain.collect

import com.lucianbc.receiptscan.domain.receipts.ReceiptId
import io.reactivex.Single

interface CollectRepository {
    fun getReceipt(receiptId: ReceiptId): Single<Receipt>
}