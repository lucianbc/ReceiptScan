package com.lucianbc.receiptscan

import com.lucianbc.receiptscan.domain.extract.ReceiptDefaults
import com.lucianbc.receiptscan.domain.model.Category
import java.util.*

val AndroidTestDefaults = object : ReceiptDefaults {
    override val currency = Currency.getInstance("RON")
    override val category = Category.Coffee
}
