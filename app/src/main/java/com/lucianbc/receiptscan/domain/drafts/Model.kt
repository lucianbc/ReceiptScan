package com.lucianbc.receiptscan.domain.drafts

import com.lucianbc.receiptscan.domain.extract.DraftId
import com.lucianbc.receiptscan.domain.model.Category
import java.util.*

data class DraftListItem (
    val id: Long,
    val creationTimestamp: Date
)

data class Draft (
    val id: DraftId,
    val merchantName: String?,
    val date: Date,
    val total: Float?,
    val currency: Currency,
    val category: Category,
    val products: List<Product>,
    val ocrElements: List<OcrElement>
)

data class Product (
    var name: String,
    var price: Float,
    val id: Long
)

data class OcrElement (
    val text: String,
    val top: Int,
    val bottom: Int,
    val left: Int,
    val right: Int,
    val id: Long
)