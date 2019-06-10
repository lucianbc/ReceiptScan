package com.lucianbc.receiptscan.domain.service

import com.lucianbc.receiptscan.domain.model.OcrElement
import com.lucianbc.receiptscan.domain.model.ProductDraft
import com.lucianbc.receiptscan.domain.model.RawReceipt
import com.lucianbc.receiptscan.domain.model.height
import com.lucianbc.receiptscan.util.Just
import com.lucianbc.receiptscan.util.None
import com.lucianbc.receiptscan.util.Optional

class ProductsAndTotalStrategy(private val receipt: RawReceipt) {
    private val boundaries: Boundaries

    private var lastKey: Optional<OcrElement> = None

    private val totalMarkRegex = "total|ammount|summe".toRegex()

    private val keyPriceResults = mutableListOf<ResultObj>()

    init {
        boundaries = boundaries(receipt)
    }

    fun execute(): Pair<Float?, List<ProductDraft>> {
        walkAndProcess()
        return makeResult(null)
    }

    private fun makeResult(draftId: Long?): Pair<Float?, List<ProductDraft>> {
        val price = keyPriceResults
            .mapNotNull {
                when(it) {
                    is ResultObj.Total -> it
                    else -> null
                }
            }
            .sortedWith(compareBy({ -it.price }, { it.top }))
            .firstOrNull()

        val products = keyPriceResults
            .mapNotNull {
                when (it) {
                    is ResultObj.Product -> it
                    else -> null
                }
            }
            .filter { if (price != null) it.top < price.top else true }
            .map { ProductDraft(it.name, it.price, draftId = draftId) }
        return price?.price to products
    }

    private fun walkAndProcess() {
        for (line in receipt) {
            for (element in line) {
                val left = isAlignedToLeft(element)
                val right = isAlignedToRight(element)
                if (left && right) {
                    processKeyValue(element)
                }
                else if (left) {
                    processKey(element)
                }
                else if (right) {
                    processPrice(element)
                }
            }
        }
    }

    private fun processPrice(priceElement: OcrElement) {
        val price = parseNumber(priceElement.text)
        price?.let {
            val mLastKey = lastKey
            if (mLastKey is Just) {
                val keyElement = mLastKey.value
                if (priceElement.top - keyElement.bottom < 0.5 * priceElement.height) {
                    makeResult(keyElement, it)
                }
                else {
                    lastKey = None
                }
            }
        }
    }

    private fun processKey(element: OcrElement) {
        val digitCount = element.text.count { it.isDigit() }
        if (digitCount < 0.3 * element.text.length) {
            lastKey = Just(element)
        }
    }

    private fun processKeyValue(element: OcrElement) {
        val price = parseNumber(element.text)
        price?.let {
            val name = element.text.split(" ").take(3).joinToString(" ")
            if (name.length > 1) {
                makeResult(name, price, element)
            }
        }
    }

    private fun makeResult(key: String, price: Float, element: OcrElement) {
        val keyLowercase = element.text.toLowerCase()
        if (keyLowercase.contains(totalMarkRegex)) {
            keyPriceResults.add(ResultObj.Total(price, element.top))
        } else {
            keyPriceResults.add(ResultObj.Product(price, key, element.top))
        }
    }

    private fun makeResult(keyElement: OcrElement, price: Float) {
        val keyLowercase = keyElement.text.toLowerCase()
        if (keyLowercase.contains(totalMarkRegex)) {
            keyPriceResults.add(ResultObj.Total(price, keyElement.top))
        } else {
            keyPriceResults.add(ResultObj.Product(price, keyElement.text, keyElement.top))
        }
    }

    private fun isAlignedToLeft(element: OcrElement): Boolean =
        (element.left - boundaries.left).toFloat() / boundaries.width < ALIGN_THRESH

    private fun isAlignedToRight(element: OcrElement): Boolean =
        (boundaries.right - element.right).toFloat() / boundaries.width < ALIGN_THRESH


    private fun boundaries(receipt: RawReceipt): Boundaries {
        val elements = receipt.flatten()
        var top = Int.MAX_VALUE
        var bottom = -1
        var left = Int.MAX_VALUE
        var right = -1
        for (a in elements) {
            if (a.top < top) top = a.top
            if (a.left < left) left = a.left
            if (a.bottom > bottom) bottom = a.bottom
            if (a.right > right) right = a.right
        }
        return Boundaries(top, bottom, left, right)
    }

    private class Boundaries(val top: Int, val bottom: Int, val left: Int, val right: Int)
    private val Boundaries.width
        get() = this.right - this.left + 1

    private sealed class ResultObj {
        class Total(val price: Float, val top: Int) : ResultObj()
        class Product(val price: Float, val name: String, val top: Int): ResultObj()
    }

    companion object {
        private const val ALIGN_THRESH = 0.1
    }
}


