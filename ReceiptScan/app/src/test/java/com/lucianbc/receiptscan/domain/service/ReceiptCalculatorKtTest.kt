package com.lucianbc.receiptscan.domain.service

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat

class ReceiptCalculatorKtTest {
    @Test
    fun testBiggestFloat() {
        val subject = "2 BUC. X 22.00 44.00C"
        val result = parseNumber(subject)
        assertEquals(44F, result)
    }

    @Test
    fun testDotOrComma() {
        val subject1 = "2.49 B"
        val subject2 = "2,49 B"
        val result1 = parseNumber(subject1)
        val result2 = parseNumber(subject2)
        val expected = 2.49F
        assertEquals(expected, result1)
        assertEquals(expected, result2)
    }

    @Test
    fun testSpaceRemoved() {
        val subject1 = "91 . 15"
        val subject2 = "91. 15"
        val subject3 = "91 .15"
        val subject4 = "91 , 15"
        val subject5 = "91, 15"
        val subject6 = "91 ,15"

        val expected = 91.15F

        assertEquals(expected, parseNumber(subject1))
        assertEquals(expected, parseNumber(subject2))
        assertEquals(expected, parseNumber(subject3))
        assertEquals(expected, parseNumber(subject4))
        assertEquals(expected, parseNumber(subject5))
        assertEquals(expected, parseNumber(subject6))
    }

    @Test
    fun testFindDate() {
        val subject = "DATA: 04-05-2019 ORA: 20:34:04"
        val result = parseDate(subject)
        val expected = SimpleDateFormat("dd-MM-yyyy").parse("04-05-2019")
        assertEquals(expected, result)
    }
}