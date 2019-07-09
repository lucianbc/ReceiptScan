package com.lucianbc.receiptscan.domain.scanner

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.model.RawReceipt
import com.lucianbc.receiptscan.domain.model.ReceiptEntity
import com.lucianbc.receiptscan.domain.viewfinder.OcrElementValue
import com.lucianbc.receiptscan.domain.viewfinder.OcrElements
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

    class Product(
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
    fun products(receiptId: Long) = products.map {
        Product(
            it.name,
            it.price,
            receiptId = receiptId
        )
    }
    fun elements(receiptId: Long) = elements.map { it.ocrElement(receiptId) }

    companion object {
        fun fromOcrElementsAndImage(image: Bitmap, elements: OcrElements): DraftValue {
            val receipt = RawReceipt.create(elements)
            val text = receipt.text
            val merchant = extractMerchant(receipt)
            val date = extractDate(text)
            val currency = extractCurrency(text)
            val (total, products) = ProductsAndTotalStrategy(receipt).execute()

            val value = DraftValue(
                merchant,
                date,
                currency,
                total,
                elements.toList(),
                image
            )

            value.products.addAll(products)

            return value
        }
    }
}
