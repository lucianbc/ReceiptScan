package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.collect.CollectRepository
import com.lucianbc.receiptscan.domain.collect.OcrElement
import com.lucianbc.receiptscan.domain.collect.Product
import com.lucianbc.receiptscan.domain.collect.Receipt
import com.lucianbc.receiptscan.infrastructure.entities.OcrElementEntity
import com.lucianbc.receiptscan.infrastructure.entities.ProductEntity
import com.lucianbc.receiptscan.infrastructure.entities.ReceiptEntity
import com.lucianbc.receiptscan.domain.receipts.ReceiptId
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import com.lucianbc.receiptscan.util.takeSingle
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function3
import javax.inject.Inject

class CollectRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : CollectRepository {
    override fun getReceipt(receiptId: ReceiptId): Single<Receipt> {
        return Flowable.zip(
            appDao.selectReceipt(receiptId),
            appDao.selectProducts(receiptId),
            appDao.selectOcrElements(receiptId),
            Function3(::create)
        ).takeSingle()
    }
}

private fun create(
    receiptEntity: ReceiptEntity,
    products: List<ProductEntity>,
    ocrElements: List<OcrElementEntity>
): Receipt {
    return receiptEntity.run {
        Receipt(
            merchantName!!,
            date!!,
            total!!,
            currency!!,
            category,
            imagePath,
            products.map {
                Product(it.name, it.price)
            },
            ocrElements.map {
                OcrElement(
                    it.text,
                    top = it.top,
                    bottom = it.bottom,
                    left = it.left,
                    right = it.right
                )
            }
        )
    }
}