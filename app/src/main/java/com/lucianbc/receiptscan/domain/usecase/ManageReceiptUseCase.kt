package com.lucianbc.receiptscan.domain.usecase

import androidx.room.Relation
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.ImagePath
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
        receipt.map { it.exported() }.takeSingle()

    fun exportPath(): Single<ImagePath> =
        receipt.map { ImagePath(it.imagePath) }.takeSingle()

    fun exportBoth(): Single<Pair<String, ImagePath>> =
        receipt.map { it.exported() to ImagePath(it.imagePath) }.takeSingle()


    private fun Value.exported(): String {
        val lines = mutableListOf(
            "Merchant: ${this.merchantName}",
            "Date: ${this.date.show()}",
            "Total: ${this.total.show()} ${this.currency.currencyCode}"
        ) + this.products.map { "${it.name}    ${it.price}" }
        return lines.joinToString("\n")
    }

    private fun<T> Flowable<T>.takeSingle() = this.take(1).singleOrError()

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