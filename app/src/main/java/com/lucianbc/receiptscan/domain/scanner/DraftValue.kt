package com.lucianbc.receiptscan.domain.scanner

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.extract.OcrElements
import com.lucianbc.receiptscan.domain.extract.ReceiptDefaults
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.viewfinder.OcrElementValue
import java.util.*
import javax.inject.Inject

class DraftValue private constructor(
    private val merchant: String?,
    private val date: Date?,
    private val currency: Currency?,
    private val total: Float?,
    private val elements: List<OcrElementValue>,
    private val category: Category,
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
            category,
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

    class Factory @Inject constructor(
        private val defaults: ReceiptDefaults
    ) {
        fun fromOcrElementsAndImage(image: Bitmap, elements: OcrElements): DraftValue {
            val receipt = RawReceipt.create(elements)
            val text = receipt.text
            val merchant = extractMerchant(receipt)
            val date = extractDate(text)
            val currency = defaults.currency
            val category = defaults.category
            val (total, products) = ProductsAndTotalStrategy(receipt).execute()
            val value = DraftValue(
                merchant,
                date,
                currency,
                total,
                elements.toList(),
                category,
                image
            )

            value.products.addAll(products)
            return value
        }
    }
}
