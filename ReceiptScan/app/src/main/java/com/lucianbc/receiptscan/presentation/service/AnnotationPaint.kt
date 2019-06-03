package com.lucianbc.receiptscan.presentation.service

import android.graphics.*
import com.lucianbc.receiptscan.domain.model.Annotation

fun paint(bitmap: Bitmap, annotations: List<Annotation>): Bitmap {
    val drawableBitmap = bitmap.copy(bitmap.config, true)
    val canvas = Canvas(drawableBitmap)
    for (a in annotations) {
        canvas.drawRect(a.rect, BOX_PAINT)
    }
    return drawableBitmap
}

val BOX_PAINT = Paint().apply {
    style = Paint.Style.FILL
    color = Color.WHITE
    alpha = 150
}

val Annotation.rect
    get() = Rect(this.left, this.top, this.right, this.bottom)