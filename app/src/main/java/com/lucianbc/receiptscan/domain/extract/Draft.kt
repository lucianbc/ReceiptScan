package com.lucianbc.receiptscan.domain.extract

import com.lucianbc.receiptscan.domain.model.Category
import java.util.*

typealias DraftId = Long

data class Draft (
    val merchantName: String?,
    val date: Date,
    val total: Float?,
    val currency: Currency,
    val category: Category,
    val products: List<Product>,
    val ocrElements: List<OcrElement>
)

data class OcrElement (
    val text: String,
    val top: Int,
    val bottom: Int,
    val left: Int,
    val right: Int
)

data class Product (
    val name: String,
    val price: Float
)

sealed class State {
    object Processing : State()
    object Idle : State()
    class Error(val err: Throwable) : State()
}