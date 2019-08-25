package com.lucianbc.receiptscan.domain.extract

import com.lucianbc.receiptscan.domain.model.Category
import java.util.*

typealias DraftId = Long
typealias OcrElements = Sequence<OcrElement>

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
) {
    val mid: Float
        get() = (bottom - top).toFloat() / 2

    val height: Int
        get() = bottom - top + 1
}

data class Product (
    val name: String,
    val price: Float
)

sealed class State {
    object Processing : State()
    object Idle : State()
    data class Error(val err: Throwable) : State()
}