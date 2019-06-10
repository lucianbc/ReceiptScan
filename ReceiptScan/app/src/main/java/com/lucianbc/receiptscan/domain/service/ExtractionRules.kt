package com.lucianbc.receiptscan.domain.service

import com.lucianbc.receiptscan.domain.model.*
import java.util.*

private const val MERCHANT_MIN_LENGTH = 2

fun extractMerchant(rawReceipt: RawReceipt) : String? {
    val linesIterator = rawReceipt.iterator()
    while (linesIterator.hasNext()) {
        val line = linesIterator.next()
        if (line.text.length < MERCHANT_MIN_LENGTH) continue

        val nextLine = if (linesIterator.hasNext()) linesIterator.next() else null

        return if (
            line.height > 1.2 * rawReceipt.averageLineHeight
            && nextLine != null
            && nextLine.text.split(" ").size < 2
        )
            line.text + " " + nextLine.text
        else
            line.text
    }
    return null
}

fun extractDate(receiptText: String) : Date =
    findDatesWithPatterns(receiptText).firstOrNull() ?: Date()


fun extract(createDraftCommand: CreateDraftCommand, filename: String): Pair<Draft, List<ProductDraft>> {

    val receipt = RawReceipt.create(createDraftCommand.elements.toList())
    val receiptText = receipt.receiptText
    val date = extractDate(receiptText)
    val (total, products) = ProductsAndTotalStrategy(receipt).execute()

    val merchant = extractMerchant(receipt)

    return Draft(filename, merchant, date, total, Currency.getInstance("RON"), true, Date()) to products
}