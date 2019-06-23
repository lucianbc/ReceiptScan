package com.lucianbc.receiptscan.domain.model

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.service.ProductsAndTotalStrategy
import com.lucianbc.receiptscan.domain.service.extractCurrency
import com.lucianbc.receiptscan.domain.service.extractDate
import com.lucianbc.receiptscan.domain.service.extractMerchant
import java.util.*

class DraftValue private constructor(
    private val merchant: String?,
    private val date: Date?,
    private val currency: Currency?,
    private val total: Float?,
    private val elements: List<OcrElementValue>,
    val image: Bitmap
) {
    private val products = mutableListOf<Product>()

    class Product (
        val name: String,
        val price: Float
    )

    fun receipt(imagePath: String) =
        ReceiptEntity(
            imagePath,
            merchant,
            date,
            total,
            currency,
            Date(),
            true
        )
    fun products(receiptId: Long) = products.map { Product(it.name, it.price, receiptId = receiptId) }
    fun elements(receiptId: Long) = elements.map { it.ocrElement(receiptId) }


    companion object {
        fun fromOcrElementsAndImage(arg: Pair<Bitmap, OcrElements>): DraftValue {
            val elements = arg.second
            val receipt = RawReceipt.create(elements)
            val text = receipt.text
            val merchant = extractMerchant(receipt)
            val date = extractDate(text)
            val currency = extractCurrency(text)
            val (total, products) = ProductsAndTotalStrategy(receipt).execute()

            val value = DraftValue(merchant, date, currency, total, arg.second.toList(), arg.first)

            value.products.addAll(products)

            return value
        }
    }
}
