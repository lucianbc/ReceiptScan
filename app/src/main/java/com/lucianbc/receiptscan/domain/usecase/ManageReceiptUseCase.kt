package com.lucianbc.receiptscan.domain.usecase

import androidx.room.Relation
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.scanner.show
import com.lucianbc.receiptscan.util.show
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class ManageReceiptUseCase(
    _receipt: Flowable<Value>
) {
    val receipt = _receipt.replay(1).autoConnect()

    data class Value(
        val id: Long,
        val merchantName: String,
        val date: Date,
        val total: Float,
        val currency: Currency,
        val category: Category,
        val imagePath: String,
        @Relation(parentColumn = "id", entityColumn = "receiptId")
        val products: List<Product>
    )

    fun exportReceipt(): Single<String> =
        receipt.map { it.exported() }.take(1).singleOrError()

    fun exportPath(): Single<String> =
        receipt.map { it.imagePath }.take(1).singleOrError()

    private fun Value.exported(): String {
        val lines = mutableListOf(
            "Merchant: ${this.merchantName}",
            "Date: ${this.date.show()}",
            "Total: ${this.total.show()} ${this.currency.currencyCode}"
        ) + this.products.map { "${it.name}    ${it.price}" }
        return lines.joinToString("\n")
    }

    class Factory @Inject constructor(
        private val draftsRepository: DraftsRepository
    ) {
        fun fetch(receiptId: Long): ManageReceiptUseCase {
            val value = draftsRepository
                .getReceipt(receiptId)
            return ManageReceiptUseCase(value)
        }
    }
}