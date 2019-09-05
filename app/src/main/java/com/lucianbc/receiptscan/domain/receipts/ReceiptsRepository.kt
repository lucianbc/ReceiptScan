package com.lucianbc.receiptscan.domain.receipts

import com.lucianbc.receiptscan.domain.model.Category
import io.reactivex.Flowable
import java.util.*

interface ReceiptsRepository {
    fun listReceipts(): Flowable<List<ReceiptListItem>>
    fun getReceipt(receiptId: ReceiptId): Flowable<Receipt>
    fun getAvailableCurrencies(): Flowable<List<Currency>>
    fun getAvailableMonths(currency: Currency): Flowable<List<Date>>
    fun getAllSpendings(currency: Currency, month: Date): Flowable<List<SpendingGroup>>
    fun getTransactions(currency: Currency, month: Date, category: Category): Flowable<List<ReceiptListItem>>
    fun getAllTransactions(currency: Currency, month: Date): Flowable<List<ReceiptListItem>>
}