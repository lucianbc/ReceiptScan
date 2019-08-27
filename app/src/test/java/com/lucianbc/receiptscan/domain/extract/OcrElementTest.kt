package com.lucianbc.receiptscan.domain.extract

import org.junit.Test

import org.junit.Assert.*

class OcrElementTest {

    @Test
    fun `vertical middle point is calculated correctly`() {
        assertEquals(mid, subject.mid)
    }

    @Test
    fun `height is calculated correctly`() {
        assertEquals(height, subject.height)
    }

    private val top = 900
    private val bottom = 1150
    private val mid = 1025f
    private val height = 251

    private val subject = OcrElement(
        "Text",
        top,
        bottom,
        1300,
        1800
    )
}