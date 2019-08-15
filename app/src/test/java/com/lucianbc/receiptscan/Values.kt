package com.lucianbc.receiptscan

import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.ReceiptDefaults
import java.util.*

val TestDefaults = object: ReceiptDefaults {
    override val currency = Currency.getInstance("RON")
    override val category = Category.Grocery
}