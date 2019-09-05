package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.receipts.*
import com.lucianbc.receiptscan.infrastructure.entities.ProductEntity
import com.lucianbc.receiptscan.infrastructure.entities.ReceiptEntity
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import java.util.*
import javax.inject.Inject

class ReceiptsRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : ReceiptsRepository {
    override fun getAvailableCurrencies(): Flowable<List<Currency>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAvailableMonths(currency: Currency): Flowable<List<Date>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllSpendings(currency: Currency, month: Date): Flowable<List<SpendingGroup>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTransactions(
        currency: Currency,
        month: Date,
        category: Category
    ): Flowable<List<ReceiptListItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllTransactions(
        currency: Currency,
        month: Date
    ): Flowable<List<ReceiptListItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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