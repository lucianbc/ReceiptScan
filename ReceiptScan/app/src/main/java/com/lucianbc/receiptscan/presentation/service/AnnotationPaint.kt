package com.lucianbc.receiptscan.presentation.service

import android.graphics.*
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.domain.model.Tag

fun paint(bitmap: Bitmap, annotations: Collection<Annotation>): Bitmap {
    val drawableBitmap = bitmap.copy(bitmap.config, true)
    val canvas = Canvas(drawableBitmap)
    for (a in annotations) {
        canvas.drawRect(a.rect, a.tag.paint)
    }
    return drawableBitmap
}

fun paint(canvas: Canvas, boxes: Sequence<Rect>) {
    for (a in boxes) {
        canvas.drawRect(a, BOX_PAINT)
    }
}

private val Tag.paint: Paint
    get() = when (this) {
        Tag.Date -> DATE_PAINT
        Tag.Merchant -> MERCHANT_PAINT
        Tag.Total -> TOTAL_PAINT
        Tag.Price -> PRICE_PAINT
        Tag.Product -> PRODUCT_PAINT
        Tag.Noise -> BOX_PAINT
    }

private val BOX_PAINT = Paint().apply {
    style = Paint.Style.FILL
    color = Color.WHITE
    alpha = 150
}

private val PRICE_PAINT = Paint().apply {
    style = Paint.Style.FILL
    color = Color.YELLOW
    alpha = 100
}

private val PRODUCT_PAINT = Paint().apply {
    style = Paint.Style.FILL
    color = Color.GREEN
    alpha = 100
}

private val TOTAL_PAINT = Paint().apply {
    style = Paint.Style.FILL
    color = Color.MAGENTA
    alpha = 100
}

private val MERCHANT_PAINT = Paint().apply {
    style = Paint.Style.FILL
    color = Color.CYAN
    alpha = 100
}

private val DATE_PAINT = Paint().apply {
    style = Paint.Style.FILL
    color = Color.BLUE
    alpha = 100
}

val Annotation.rect
    get() = Rect(this.left, this.top, this.right, this.bottom)