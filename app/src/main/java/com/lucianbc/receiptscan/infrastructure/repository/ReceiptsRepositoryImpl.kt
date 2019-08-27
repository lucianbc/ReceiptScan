package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.receipts.ReceiptId
import com.lucianbc.receiptscan.domain.receipts.ReceiptsRepository
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import javax.inject.Inject

class ReceiptsRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : ReceiptsRepository {

    override fun listReceipts() =
        appDao.getReceiptItems()

    override fun getReceipt(receiptId: ReceiptId) =
        appDao.getReceipt(receiptId)
}