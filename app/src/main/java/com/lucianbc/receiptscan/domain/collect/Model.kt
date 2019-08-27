package com.lucianbc.receiptscan.domain.collect

import com.lucianbc.receiptscan.domain.model.Category
import java.util.*

data class Receipt (
    val merchantName: String,
    val date: Date,
    val total: Float,
    val currency: Currency,
    val category: Category,
    val imagePath: String,
    val product: List<Product>,
    val ocrElements: List<OcrElement>
)

data class Product (
    val name: String,
    val price: Float
)

data class OcrElement (
    val text: String,
    val top: Int,
    val bottom: Int,
    val left: Int,
    val right: Int
)