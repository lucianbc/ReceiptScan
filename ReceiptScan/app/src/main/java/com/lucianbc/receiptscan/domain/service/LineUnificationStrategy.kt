package com.lucianbc.receiptscan.domain.service

import com.lucianbc.receiptscan.domain.model.ScanAnnotations
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import java.util.*
import kotlin.collections.ArrayList

typealias Line = ScanAnnotations

interface LineUnificationStrategy {
    fun unify(boxes: ScanAnnotations): Iterable<Line>
}

class ThresholdBetweenNeighbors(private val threshold: Float): LineUnificationStrategy {
    override fun unify(boxes: ScanAnnotations): Iterable<Line> {
        val sorted = boxes.sortedBy { t -> t.mid }
        val unifiedLines = LinkedList<Line>()

        var currentLine = ArrayList<ScanInfoBox>()
        val boxesIterator = sorted.iterator()

        if (boxesIterator.hasNext()) {
            var lastBox = boxesIterator.next()
            currentLine.add(lastBox)

            while (boxesIterator.hasNext()) {
                val crtBox = boxesIterator.next()
                val threshVal = threshold * (crtBox.height + lastBox.height).toFloat() / 2
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

    private val ScanInfoBox.mid: Float
        get() = (this.bottom + this.top).toFloat() / 2

    private val ScanInfoBox.height: Int
        get() = this.bottom - this.top + 1
}