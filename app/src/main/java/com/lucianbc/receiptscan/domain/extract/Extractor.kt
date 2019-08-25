package com.lucianbc.receiptscan.domain.extract

import com.lucianbc.receiptscan.domain.model.RawReceipt
import com.lucianbc.receiptscan.domain.scanner.ProductsAndTotalStrategy
import com.lucianbc.receiptscan.domain.scanner.extractDate
import com.lucianbc.receiptscan.domain.scanner.extractMerchant
import javax.inject.Inject

class Extractor @Inject constructor(
    private val defaults: ReceiptDefaults
) {
    operator fun invoke(elements: OcrElements): Draft {
        val receipt = RawReceipt.create(elements)
        val text = receipt.text
        val merchant = extractMerchant(receipt)
        val date = extractDate(text)
        val currency = defaults.currency
        val category = defaults.category
        val (total, products) = ProductsAndTotalStrategy(receipt).execute()
        return Draft (
            merchant,
            date,
            total,
            currency,
            category,
            products.map { Product(it.name, it.price) },
            elements.map { OcrElement(it.text, it.top, it.bottom, it.left, it.right) }.toList()
        )
    }
}