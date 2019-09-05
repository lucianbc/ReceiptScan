package com.lucianbc.receiptscan.domain.receipts

import com.lucianbc.receiptscan.domain.model.Category
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
    val productEntities: List<Product>
)

data class Product (
    val name: String,
    val price: Float
)

data class SpendingGroup (
    val group: Group,
    val total: Float,
    val currency: Currency
)

sealed class Group {
    data class Categorized(val value: Category): Group()
    object Total: Group()
}