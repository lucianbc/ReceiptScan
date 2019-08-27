package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.model.ProductEntity
import com.lucianbc.receiptscan.domain.model.ReceiptEntity
import com.lucianbc.receiptscan.domain.receipts.Product
import com.lucianbc.receiptscan.domain.receipts.Receipt
import com.lucianbc.receiptscan.domain.receipts.ReceiptId
import com.lucianbc.receiptscan.domain.receipts.ReceiptsRepository
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ReceiptsRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : ReceiptsRepository {

    override fun listReceipts() =
        appDao.getReceiptItems()

    override fun getReceipt(receiptId: ReceiptId): Flowable<Receipt> {
        return appDao.run {
            Flowable.zip(
                selectReceipt(receiptId),
                selectProducts(receiptId),
                BiFunction(::create)
            )
        }
    }
}

private fun create(
    receiptEntity: ReceiptEntity,
    products: List<ProductEntity>
): Receipt {
    return receiptEntity.run {
        Receipt(
            id!!,
            merchantName!!,
            date!!,
            total!!,
            currency!!,
            category,
            imagePath,
            products.map { Product(it.name, it.price) }
        )
    }
}