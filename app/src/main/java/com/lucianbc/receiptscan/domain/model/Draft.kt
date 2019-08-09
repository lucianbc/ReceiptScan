package com.lucianbc.receiptscan.domain.model

import java.util.*

data class Draft(
    val imagePath: String,
    val merchantName: String?,
    val date: Date?,
    val total: Float?,
    val currency: Currency?,
    val category: Category,
    val creationTimestamp: Date,
    var id: Long
) {
    fun entity() =
        ReceiptEntity(
            imagePath,
            merchantName,
            date,
            total,
            currency,
            category,
            creationTimestamp,
            true,
            id
        )
}