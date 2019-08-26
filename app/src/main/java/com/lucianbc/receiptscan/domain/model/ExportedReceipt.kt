package com.lucianbc.receiptscan.domain.model

import androidx.room.Relation
import java.util.*

data class ExportedReceipt(
    val id: Long,
    val merchantName: String,
    val date: Date,
    val total: Float,
    val currency: Currency,
    val category: Category,
    val imagePath: String,
    @Relation(parentColumn = "id", entityColumn = "receiptId")
    val productEntities: List<ProductEntity>,
    @Relation(parentColumn = "id", entityColumn = "receiptId")
    val ocrElements: List<OcrElementEntity>
)