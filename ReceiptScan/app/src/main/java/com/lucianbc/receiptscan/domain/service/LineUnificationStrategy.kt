package com.lucianbc.receiptscan.domain.service

import com.lucianbc.receiptscan.domain.model.OcrElement
import java.util.*
import kotlin.collections.ArrayList

interface LineUnificationStrategy {
    fun unify(boxes: List<OcrElement>): List<List<OcrElement>>
}

class ThresholdBetweenNeighbors: LineUnificationStrategy {
    override fun unify(boxes: List<OcrElement>): List<List<OcrElement>> {
        val sorted = boxes.sortedBy { t -> t.mid }
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

        return unifiedLines
    }

    private val OcrElement.mid: Float
        get() = (this.bottom + this.top).toFloat() / 2

    private val OcrElement.height: Int
        get() = this.bottom - this.top + 1

    companion object {
        const val THRESHOLD = 0.5F
    }
}