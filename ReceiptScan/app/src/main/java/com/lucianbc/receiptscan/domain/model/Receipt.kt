package com.lucianbc.receiptscan.domain.model

import java.util.*

data class Receipt(
    val merchant: String,
    val date: String,
    val currency: Currency,
    val total: Double
) {
    data class Item(
        val name: String,
        val price: Double
    )
}