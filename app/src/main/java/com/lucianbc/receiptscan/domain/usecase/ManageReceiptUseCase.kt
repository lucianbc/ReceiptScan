package com.lucianbc.receiptscan.domain.usecase

import androidx.room.Relation
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import io.reactivex.Flowable
import java.util.*
import javax.inject.Inject

class ManageReceiptUseCase(
    val receipt: Flowable<Value>
) {
    data class Value(
        val id: Long,
        val merchantName: String,
        val date: Date,
        val total: Float,
        val currency: Currency,
        val category: Category,
        @Relation(parentColumn = "id", entityColumn = "receiptId")
        val products: List<Product>
    )

    class Factory @Inject constructor(
        private val draftsRepository: DraftsRepository
    ) {
        fun fetch(receiptId: Long): ManageReceiptUseCase {
            val value = draftsRepository
                .getReceipt(receiptId)
                .replay(1)
                .autoConnect()
            return ManageReceiptUseCase(value)
        }
    }
}