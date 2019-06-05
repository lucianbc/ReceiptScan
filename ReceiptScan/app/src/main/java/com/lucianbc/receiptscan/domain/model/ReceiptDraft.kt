package com.lucianbc.receiptscan.domain.model

import java.util.*

data class ReceiptDraft (
    val merchantName: String?,
    val date: Date?,
    val currency: Currency?,
    val total: Float?,
    val id: Long
)
