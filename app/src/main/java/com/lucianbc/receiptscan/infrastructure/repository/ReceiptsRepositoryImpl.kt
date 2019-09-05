package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.receipts.*
import com.lucianbc.receiptscan.infrastructure.entities.ProductEntity
import com.lucianbc.receiptscan.infrastructure.entities.ReceiptEntity
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.zipWith
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReceiptsRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : ReceiptsRepository {
    override fun getAvailableCurrencies() =
        appDao.selectCurrencies()

    override fun getAvailableMonths(currency: Currency): Flowable<List<Date>> {
        val sft = SimpleDateFormat("MM-yyyy", Locale.US)
        return appDao.selectMonths(currency).map {
            it.map { date -> sft.format(date).run(sft::parse) }.toSet().toList()
        }
    }

    override fun getAllSpendings(currency: Currency, month: Date): Flowable<List<SpendingGroup>> {
        val categorized = appDao.selectSpendingsByCategory(currency, month)
        val total = appDao.selectAllSpendingTotal(currency, month)
        return categorized.zipWith(total)
            .map {
                val totalsc = SpendingGroup(Group.Total, it.second, currency)
                val categorizedsc = it.first.map { sc ->
                    SpendingGroup(Group.Categorized(sc.category), sc.total, sc.currency)
                }
                listOf(totalsc) + categorizedsc
            }
    }

    override fun getTransactions(
        currency: Currency,
        month: Date,
        category: Category
    ): Flowable<List<ReceiptListItem>> {
        return appDao.getTransactionsForCategory(currency, month, category)
    }

    override fun getAllTransactions(
        currency: Currency,
        month: Date
    ): Flowable<List<ReceiptListItem>> {
        return appDao.getAllTransactions(currency, month)
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

data class SpendingsCategory (
    val category: Category,
    val total: Float,
    val currency: Currency
)

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