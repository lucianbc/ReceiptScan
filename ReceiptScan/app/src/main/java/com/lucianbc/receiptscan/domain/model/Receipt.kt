package com.lucianbc.receiptscan.domain.model

import java.util.*

data class Receipt (
    val merchant: String,
    val date: Date,
    val currency: Currency,
    val total: Double
)
