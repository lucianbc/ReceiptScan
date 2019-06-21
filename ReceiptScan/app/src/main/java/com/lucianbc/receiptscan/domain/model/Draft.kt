package com.lucianbc.receiptscan.domain.model

import java.util.*

data class Draft (
    val imagePath: String,
    val merchantName: String?,
    val date: Date?,
    val total: Float?,
    val currency: Currency?,
    val creationTimestamp: Date,
    var id: Long
) {
    fun receipt() =
        ReceiptEntity(
            imagePath,
            merchantName,
            date,
            total,
            currency,
            creationTimestamp,
            true,
            id
        )
}