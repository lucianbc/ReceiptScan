package com.lucianbc.receiptscan.domain.receipts

import com.lucianbc.receiptscan.domain.model.ImagePath
import io.reactivex.Flowable
import io.reactivex.Single

interface ReceiptsUseCase {
    fun list(): Flowable<List<ReceiptListItem>>
    fun fetch(receiptId: ReceiptId): Manage

    interface Manage {
        val receipt: Flowable<Receipt>
        fun exportReceipt(): Single<String>
        fun exportPath(): Single<ImagePath>
        fun exportBoth(): Single<Pair<String, ImagePath>>
    }
}