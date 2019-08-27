package com.lucianbc.receiptscan.domain.receipts

import androidx.room.Relation
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.ProductEntity
import java.util.*

typealias ReceiptId = Long

data class ReceiptListItem (
    val id: ReceiptId,
    val merchantName: String,
    val total: Float
)

data class Receipt (
    val id: ReceiptId,
    val merchantName: String,
    val date: Date,
    val total: Float,
    val currency: Currency,
    val category: Category,
    val imagePath: String,
    @Relation(parentColumn = "id", entityColumn = "receiptId")
    val productEntities: List<ProductEntity>
)