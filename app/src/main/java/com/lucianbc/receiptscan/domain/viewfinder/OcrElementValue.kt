package com.lucianbc.receiptscan.domain.viewfinder

import com.lucianbc.receiptscan.domain.model.OcrElement

data class OcrElementValue(
    val text: String,
    val top: Int,
    val left: Int,
    val bottom: Int,
    val right: Int
) {
    val mid: Float
        get() = (this.bottom + this.top).toFloat() / 2

    val height: Int
        get() = this.bottom - this.top + 1

    fun ocrElement(receiptId: Long) =
        OcrElement(
            text,
            top,
            left,
            bottom,
            right,
            receiptId
        )
}