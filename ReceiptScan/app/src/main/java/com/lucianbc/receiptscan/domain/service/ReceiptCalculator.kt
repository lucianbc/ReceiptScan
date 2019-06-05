package com.lucianbc.receiptscan.domain.service

import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.model.Annotation
import java.util.*

typealias CategorizedAnnotations = Map<Tag, List<Annotation>>

fun computeReceipt(draftId: Long, annotations: List<Annotation>): ReceiptDraftWithProducts {
    val data = annotations
        .groupBy { it.tag }

    return ReceiptDraftWithProducts(
        ReceiptDraft(
            createMerchant(data),
            createDate(data),
            createCurrency(data),
            createTotal(data),
            draftId),
        createProducts(data, draftId)
    )
}

private fun createMerchant(data: CategorizedAnnotations): String? =
    data[Tag.Merchant]?.joinToString(" ") { it.text }

private fun createDate(data: CategorizedAnnotations): Date? =
    data[Tag.Date]?.mapNotNull { parseDate(it.text) }?.firstOrNull()

private fun createCurrency(data: CategorizedAnnotations): Currency? =
    Currency.getInstance("RON")

private fun createTotal(data: CategorizedAnnotations): Float? =
    data[Tag.Total]
        ?.mapNotNull { parseNumber(it.text) }
        ?.sortedDescending()
        ?.firstOrNull()

private fun createProducts(data: CategorizedAnnotations, draftId: Long): List<ProductDraft> {
    val products = data[Tag.Product].orEmpty()
        .sortedBy { it.top }
        .asSequence()
        .map { it.text }
    val prices = data[Tag.Price].orEmpty()
        .sortedBy { it.top }
        .asSequence()
        .map { parseNumber(it.text) ?: -0F }

    return products
        .zip(prices)
        .map { ProductDraft(it.first, it.second, draftId = draftId) }
        .toList()
}

fun parseNumber(string: String): Float? =
    Regex("[+-]?([0-9]*[.,])?[0-9]+")
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