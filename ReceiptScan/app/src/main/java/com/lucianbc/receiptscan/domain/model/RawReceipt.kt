package com.lucianbc.receiptscan.domain.model

import java.util.*
import kotlin.collections.ArrayList

class RawReceipt(private val lines: List<Line>): Iterable<RawReceipt.Line> {
    override fun iterator(): Iterator<Line> = lines.iterator()

    class Line(private val elements: List<OcrElement>) : Iterable<OcrElement> {
        override fun iterator() = elements.iterator()
        val text
            get() = elements.joinToString(" ") { it.text }
        val height
            get() = elements.map { it.height }.average()
    }

    val averageLineHeight
        get() = this.lines.map { it.height }.average()

    val text by lazy {
        lines.joinToString("\n") { it.joinToString("\t") { t -> t.text } }
    }

    companion object {
        private const val THRESHOLD = 0.5F
        fun create(elements: List<OcrElement>): RawReceipt {
            val sorted = elements.sortedBy { t -> t.mid }
            val unifiedLines = LinkedList<Line>()

            var currentLine = ArrayList<OcrElement>()
            val boxesIterator = sorted.iterator()

            if (boxesIterator.hasNext()) {
                var lastBox = boxesIterator.next()
                currentLine.add(lastBox)

                while (boxesIterator.hasNext()) {
                    val crtBox = boxesIterator.next()
                    val threshVal = THRESHOLD * (crtBox.height + lastBox.height).toFloat() / 2
                    if (crtBox.mid - lastBox.mid < threshVal) {
                        currentLine.add(crtBox)
                    } else {
                        val sortedLine = currentLine.sortedBy { it.left }
                        unifiedLines.add(Line(sortedLine))
                        currentLine = ArrayList()
                        currentLine.add(crtBox)
                    }
                    lastBox = crtBox
                }
            }

            if (currentLine.isNotEmpty()) {
                unifiedLines.add(Line(currentLine))
            }

            return RawReceipt(unifiedLines)
        }
    }
}

val OcrElement.mid: Float
    get() = (this.bottom + this.top).toFloat() / 2

val OcrElement.height: Int
    get() = this.bottom - this.top + 1