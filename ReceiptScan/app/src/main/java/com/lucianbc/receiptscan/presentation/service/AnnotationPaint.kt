package com.lucianbc.receiptscan.presentation.service

import android.graphics.*
import com.lucianbc.receiptscan.domain.model.OcrElement

fun paint(bitmap: Bitmap, ocrElements: Collection<OcrElement>): Bitmap {
    val drawableBitmap = bitmap.copy(bitmap.config, true)
    val canvas = Canvas(drawableBitmap)
    for (a in ocrElements) {
        canvas.drawRect(a.rect, BOX_PAINT)
    }
    return drawableBitmap
}

fun paint(canvas: Canvas, boxes: Sequence<Rect>) {
    for (a in boxes) {
        canvas.drawRect(a, BOX_PAINT)
    }
}

private val BOX_PAINT = Paint().apply {
    style = Paint.Style.FILL
    color = Color.WHITE
    alpha = 150
}

private val OcrElement.rect
    get() = Rect(left, top, right, bottom)