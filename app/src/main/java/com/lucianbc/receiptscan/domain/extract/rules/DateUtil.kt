package com.lucianbc.receiptscan.domain.extract.rules

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

private const val DIGIT_MISTAKES = "[\\doO]"

val formats = mapOf(
    "$DIGIT_MISTAKES{8}" to "yyyyMMdd",
    "$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{4}" to "dd/MM/yyyy",
    "$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{4}" to "dd-MM-yyyy",
    "$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{4}" to "dd.MM.yyyy",
    "$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{2}(?!\\d)" to "dd/MM/yy",
    "$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{2}(?!\\d)" to "dd-MM-yy",
    "$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{2}(?!\\d)" to "dd.MM.yy",
    "$DIGIT_MISTAKES{4}/$DIGIT_MISTAKES{2}/$DIGIT_MISTAKES{2}" to "yyyy/MM/dd",
    "$DIGIT_MISTAKES{4}-$DIGIT_MISTAKES{2}-$DIGIT_MISTAKES{2}" to "yyyy-MM-dd",
    "$DIGIT_MISTAKES{4}\\.$DIGIT_MISTAKES{2}\\.$DIGIT_MISTAKES{2}" to "yyyy.MM.dd"
)

fun findDatesWithPatterns(searchedString: String): Sequence<Date> {
    var results = sequenceOf<Pair<String, String>>()
    val fakeZeros = "Oo".toRegex()
    for ((regex, format) in formats) {
        val result = regex.toRegex().findAll(searchedString)
        val seq = result.map { it.value.replace(fakeZeros, "0") to format }
        results += seq
    }

    val now = Date()

    return results
        .mapNotNull {
            try {
                SimpleDateFormat(it.second, Locale.US).parse(it.first)
            } catch (e: ParseException) {
                null
            }
        }
        .sortedBy { abs(it.time - now.time) }
}

fun parseDate(dateString: String): Date? {
    val lowerDate = dateString.toLowerCase()

    return findDatesWithPatterns(lowerDate).firstOrNull()
}

fun Date?.show(): String = if (this != null ) format.format(this) else ""

private val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)