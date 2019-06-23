package com.lucianbc.receiptscan.presentation.scanner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.lucianbc.receiptscan.domain.model.OcrElementValue
import com.lucianbc.receiptscan.presentation.service.paint

class OcrOverlay (
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {
    private val graphics = mutableSetOf<OcrElementValue>()
    private val lock = Any()

    fun submitOcrElements(elements: Sequence<OcrElementValue>) {
        synchronized(lock) {
            graphics.clear()
            graphics.addAll(elements)
            postInvalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        synchronized(lock) {
            paint(canvas!!, graphics.asSequence().map { it.rect.transform() })
        }
    }

    private val OcrElementValue.rect
        get() = Rect(this.left, this.top, this.right, this.bottom)

    // this transformation is specific to the cropping done on my device.
    // TODO: Implement a formula for this transformation that works on any device
    private fun Rect.transform(): Rect {
        return Rect(this.left + LEFT_OFFSET, this.top + TOP_OFFSET, this.right + LEFT_OFFSET, this.bottom + TOP_OFFSET)
    }

    companion object {
        private const val LEFT_OFFSET = -200
        private const val TOP_OFFSET = 40
    }
}