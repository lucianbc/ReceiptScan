package com.lucianbc.receiptscan.domain.receipts

data class ReceiptListItem (
    val id: Long,
    val merchantName: String,
    val total: Float
)