package com.lucianbc.receiptscan.domain.model

import com.lucianbc.receiptscan.domain.extract.OcrElements
import com.lucianbc.receiptscan.domain.viewfinder.OcrElementValue
import java.util.*
import kotlin.collections.ArrayList

class RawReceipt(private val lines: List<Line>) : Iterable<RawReceipt.Line> {
    override fun iterator(): Iterator<Line> = lines.iterator()

    class Line(private val elements: List<OcrElementValue>) : Iterable<OcrElementValue> {
        override fun iterator() = elements.iterator()
        val text by lazy { elements.joinToString(" ") { it.text } }
        val height by lazy { elements.map { it.height }.average() }
        val top by lazy { elements.map { it.top }.min()!! }
        val bottom by lazy { elements.map { it.bottom }.max()!! }
    }

    val averageLineHeight by lazy { this.lines.map { it.height }.average() }

    val text by lazy { lines.joinToString("\n") { it.joinToString("\t") { t -> t.text } } }

    companion object {
        private const val THRESHOLD = 0.5F
        fun create(elements: OcrElements): RawReceipt {
            val sorted = elements.sortedBy { t -> t.mid }
            val unifiedLines = LinkedList<Line>()

            var currentLine = ArrayList<OcrElementValue>()
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

val OcrElement.height: Int
    get() = this.bottom - this.top + 1
