package com.lucianbc.receiptscan.util

import org.junit.Assert.*
import org.junit.Test

class ExtensionsKtTest {
    @Test
    fun testWorks() {
        var check = false
        val x = 10
        x.guard(x == 10) {
            check = true
        }
        assertTrue(check)
    }

    @Test
    fun testBlocks() {
        var check = false
        val x = 10
        x.guard(x != 10) {
            check = true
        }
        assertFalse(check)
    }

    @Test
    fun testNotNull() {
        var check = false
        val x = null
        x.guard(x != null) {
            check = true
        }
        assertFalse(check)
    }

    @Test
    fun testNotNull2() {
        var check = false
        val x = 10
        x.guard(x != null) {
            check = true
        }
        assertTrue(check)
    }
}