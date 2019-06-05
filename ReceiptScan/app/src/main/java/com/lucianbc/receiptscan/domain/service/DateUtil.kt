package com.lucianbc.receiptscan.domain.service

import java.text.SimpleDateFormat
import java.util.*

val formats = mapOf(
    "\\d{8}" to "yyyyMMdd",
    "\\d{1,2}/\\d{1,2}/\\d{4}" to "dd/MM/yyyy",
    "\\d{1,2}-\\d{1,2}-\\d{4}" to "dd-MM-yyyy",
    "\\d{1,2}.\\d{1,2}.\\d{4}" to "dd.MM.yyyy",
    "\\d{4}/\\d{1,2}/\\d{1,2}" to "yyyy/MM/dd",
    "\\d{4}-\\d{1,2}-\\d{1,2}" to "yyyy-MM-dd",
    "\\d{1,2}\\s[a-z]{3}\\s\\d{4}" to "dd MMM yyyy",
    "\\d{1,2}\\s[a-z]{4,}\\s\\d{4}" to "dd MMMM yyyy",
    "\\d{12}" to "yyyyMMddHHmm",
    "\\d{8}\\s\\d{4}" to "yyyyMMdd HHmm",
    "\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}" to "dd/MM/yyyy HH:mm",
    "\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}" to "dd-MM-yyyy HH:mm",
    "\\d{1,2}.\\d{1,2}.\\d{4}\\s\\d{1,2}:\\d{2}" to "dd.MM.yyyy HH:mm",
    "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}" to "yyyy-MM-dd HH:mm",
    "\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}" to "yyyy/MM/dd HH:mm",
    "\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}" to "dd MMM yyyy HH:mm",
    "\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}" to "dd MMMM yyyy HH:mm",
    "\\d{14}" to "yyyyMMddHHmmss",
    "\\d{8}\\s\\d{6}" to "yyyyMMdd HHmmss",
    "\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}" to "dd/MM/yyyy HH:mm:ss",
    "\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}" to "dd-MM-yyyy HH:mm:ss",
    "\\d{1,2}.\\d{1,2}.\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}" to "dd.MM.yyyy HH:mm:ss",
    "\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}" to "yyyy/MM/dd HH:mm:ss",
    "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}" to "yyyy-MM-dd HH:mm:ss",
    "\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}" to "dd MMM yyyy HH:mm:ss",
    "\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}" to "dd MMMM yyyy HH:mm:ss"
)

private fun determineFormatAndValue(dateString: String): Pair<String, String>? {
    for ((regex, format) in formats) {
        val result = regex.toRegex().find(dateString)
        if (result != null) {
            return format to result.value
        }
    }
    return null
}

fun parseDate(dateString: String): Date? {
    val lowerDate = dateString.toLowerCase()
    val result = determineFormatAndValue(lowerDate)
    return if (result != null) SimpleDateFormat(result.first, Locale.US).parse(result.second) else null
}


fun Date.show(): String = format.format(this)

private val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)