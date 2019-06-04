package com.lucianbc.receiptscan.domain.service

import org.junit.Assert.*
import org.junit.Test

class ReceiptCalculatorKtTest {
    @Test
    fun testBiggestFloat() {
        val subject = "2 BUC. X 22.00 44.00C"
        val result = parseNumber(subject)
        assertEquals(44F, result)
    }
}