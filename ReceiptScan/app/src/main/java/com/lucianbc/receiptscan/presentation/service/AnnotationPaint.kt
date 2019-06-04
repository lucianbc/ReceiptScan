package com.lucianbc.receiptscan.presentation.service

import android.graphics.*
import com.lucianbc.receiptscan.domain.model.Annotation

fun paint(bitmap: Bitmap, annotations: Collection<Annotation>): Bitmap {
    val drawableBitmap = bitmap.copy(bitmap.config, true)
    val canvas = Canvas(drawableBitmap)
    paint(canvas, annotations.asSequence().map { it.rect })
    return drawableBitmap
}

fun paint(canvas: Canvas, boxes: Sequence<Rect>) {
    for (a in boxes) {
        canvas.drawRect(a, BOX_PAINT)
    }
}

val BOX_PAINT = Paint().apply {
    style = Paint.Style.FILL
    color = Color.WHITE
    alpha = 150
}

val Annotation.rect
    get() = Rect(this.left, this.top, this.right, this.bottom)