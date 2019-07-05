package com.lucianbc.receiptscan.domain.scanner

import com.lucianbc.receiptscan.domain.model.*
import java.util.*

private const val MERCHANT_MIN_LENGTH = 2

fun extractMerchant(rawReceipt: RawReceipt) : String? {
    val linesIterator = rawReceipt.iterator()
    while (linesIterator.hasNext()) {
        val line = linesIterator.next()
        if (line.text.length < MERCHANT_MIN_LENGTH) continue

        val nextLine = if (linesIterator.hasNext()) linesIterator.next() else null

        val heightThreshold = 1.2 * rawReceipt.averageLineHeight

        return if (
            line.height > heightThreshold
            && nextLine != null
            && nextLine.text.split(" ").size < 2
            && nextLine.height > heightThreshold
            && nextLine.top - line.bottom < rawReceipt.averageLineHeight
        )
            line.text + " " + nextLine.text
        else
            line.text
    }
    return null
}

fun extractDate(receiptText: String) : Date =
    findDatesWithPatterns(receiptText).firstOrNull() ?: Date()

fun extractCurrency(receiptText: String): Currency = Currency.getInstance("RON")

fun parseNumber(string: String): Float? =
    Regex("[+-]?([0-9]*[.,])[0-9]+")
        .findAll(string.removeSpaceInFloat())
        .map { it.value.replace(',', '.') }
        .mapNotNull { it.toFloatOrNull() }
        .sortedDescending()
        .firstOrNull()


private val spaceBefore = "(\\d)\\s([.,])".toRegex()
private val spaceAfter = "([.,])\\s(\\d)".toRegex()

private fun String.removeSpaceInFloat(): String = this
    .replace(spaceBefore, "$1$2")
    .replace(spaceAfter, "$1$2")