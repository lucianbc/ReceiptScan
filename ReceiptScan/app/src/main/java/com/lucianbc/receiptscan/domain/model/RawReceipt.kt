package com.lucianbc.receiptscan.domain.model

import java.util.*
import kotlin.collections.ArrayList

inline class RawReceipt (val lines: List<List<OcrElement>>) {

    companion object {
        private const val THRESHOLD = 0.5F
        fun create(elements: List<OcrElement>): RawReceipt {
            val sorted = elements.sortedBy { t -> t.mid }
            val unifiedLines = LinkedList<List<OcrElement>>()

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
                        unifiedLines.add(sortedLine)
                        currentLine = ArrayList()
                        currentLine.add(crtBox)
                    }
                    lastBox = crtBox
                }
            }

            if (currentLine.isNotEmpty()) {
                unifiedLines.add(currentLine)
            }

            return RawReceipt(unifiedLines)
        }
    }
}

private val OcrElement.mid: Float
    get() = (this.bottom + this.top).toFloat() / 2

private val OcrElement.height: Int
    get() = this.bottom - this.top + 1
