package com.lucianbc.receiptscan.domain.model

import org.junit.Assert.*
import org.junit.Test

class MappersTest {
    @Test
    fun ocrElementToAnnotation() {
        val tag = Tag.Noise
        val ocrElement = OcrElement("Text", 1, 2, 3, 4)
        val annotation = ocrElement.toAnnotation(tag)

        assertEquals(tag, annotation.tag)
        assertEquals(ocrElement.left, annotation.left)
        assertEquals(ocrElement.right, annotation.right)
        assertEquals(ocrElement.top, annotation.top)
        assertEquals(ocrElement.bottom, annotation.bottom)
    }
}