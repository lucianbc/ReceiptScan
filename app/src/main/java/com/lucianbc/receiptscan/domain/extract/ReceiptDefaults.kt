package com.lucianbc.receiptscan.domain.extract

import com.lucianbc.receiptscan.domain.model.Category
import java.util.*

interface ReceiptDefaults {
    val currency: Currency
    val category: Category
}