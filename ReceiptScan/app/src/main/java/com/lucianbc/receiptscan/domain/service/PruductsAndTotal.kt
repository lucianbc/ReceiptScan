package com.lucianbc.receiptscan.domain.service

import com.lucianbc.receiptscan.domain.model.DraftValue
import com.lucianbc.receiptscan.domain.scanner.OcrElementValue
import com.lucianbc.receiptscan.domain.model.RawReceipt
import com.lucianbc.receiptscan.util.Just
import com.lucianbc.receiptscan.util.None
import com.lucianbc.receiptscan.util.Optional

class ProductsAndTotalStrategy(private val receipt: RawReceipt) {
    private val horizontalBorders: HorizontalBorders

    private var lastKey: Optional<OcrElementValue> = None

    private val totalMarkRegex = "total|ammount|summe".toRegex()

    private val keyPriceResults = mutableListOf<ResultObj>()

    init {
        horizontalBorders = boundaries(receipt)
    }

    fun execute(): Pair<Float?, List<DraftValue.Product>> {
        walkAndProcess()
        return makeResult()
    }

    private fun makeResult(): Pair<Float?, List<DraftValue.Product>> {
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
            .map { DraftValue.Product(it.name, it.price) }
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

    private fun processPrice(priceElement: OcrElementValue) {
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

    private fun processKey(element: OcrElementValue) {
        val digitCount = element.text.count { it.isDigit() }
        if (digitCount < 0.3 * element.text.length) {
            lastKey = Just(element)
        }
    }

    private fun processKeyValue(element: OcrElementValue) {
        val price = parseNumber(element.text)
        price?.let {
            val name = element.text.split(" ").take(3).joinToString(" ")
            if (name.length > 1) {
                makeResult(name, price, element)
            }
        }
    }

    private fun makeResult(key: String, price: Float, element: OcrElementValue) {
        val keyLowercase = element.text.toLowerCase()
        if (keyLowercase.contains(totalMarkRegex)) {
            keyPriceResults.add(ResultObj.Total(price, element.top))
        } else {
            keyPriceResults.add(ResultObj.Product(price, key, element.top))
        }
    }

    private fun makeResult(keyElement: OcrElementValue, price: Float) {
        val keyLowercase = keyElement.text.toLowerCase()
        if (keyLowercase.contains(totalMarkRegex)) {
            keyPriceResults.add(ResultObj.Total(price, keyElement.top))
        } else {
            keyPriceResults.add(ResultObj.Product(price, keyElement.text, keyElement.top))
        }
    }

    private fun isAlignedToLeft(element: OcrElementValue): Boolean =
        (element.left - horizontalBorders.left).toFloat() / horizontalBorders.width < ALIGN_THRESH

    private fun isAlignedToRight(element: OcrElementValue): Boolean =
        (horizontalBorders.right - element.right).toFloat() / horizontalBorders.width < ALIGN_THRESH


    private fun boundaries(receipt: RawReceipt): HorizontalBorders {
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
        return HorizontalBorders(left, right)
    }

    private class HorizontalBorders(val left: Int, val right: Int)
    private val HorizontalBorders.width
        get() = this.right - this.left + 1

    private sealed class ResultObj {
        class Total(val price: Float, val top: Int) : ResultObj()
        class Product(val price: Float, val name: String, val top: Int): ResultObj()
    }

    companion object {
        private const val ALIGN_THRESH = 0.1
    }
}


