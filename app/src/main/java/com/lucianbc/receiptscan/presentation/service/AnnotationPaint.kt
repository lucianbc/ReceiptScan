package com.lucianbc.receiptscan.presentation.service

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

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
