package com.lucianbc.receiptscan.v2.flows

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DefaultFlowKtTest {
    @Test
    fun `it builds a flow from a block and dispatches the default value and then the rest`() {
        val f = defaultFlow(1) {
            delay(100)
            emit(2)
            delay(200)
            emit(3)
            delay(100)
            emit(4)
        }

        val l = runBlocking { f.toList() }

        assert(l == listOf(1, 2, 3, 4))
    }

    @Test
    fun `it builds a flow from another flow and dispatches the default value and then the rest`() {
        val flow = flowOf(2, 3, 4)
        val f = defaultFlow(1, flow)

        val l = runBlocking { f.toList() }

        assert(l == listOf(1, 2, 3, 4))
    }
}