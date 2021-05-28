package com.lucianbc.receiptscan.v2.flows

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


interface DefaultFlow<out T> : Flow<T> {
    val defaultValue: T
}

fun <T> defaultFlow(defaultValue: T, block: suspend FlowCollector<T>.() -> Unit): DefaultFlow<T> {
    val f = flow {
        emit(defaultValue)
        block()
    }

    return DefaultFlowImpl(defaultValue, f)
}

fun <T> defaultFlow(defaultValue: T, flow: Flow<T>): DefaultFlow<T> {
    return defaultFlow(defaultValue) {
        flow.collect(::emit)
    }
}

private class DefaultFlowImpl<T>(override val defaultValue: T, innerFlow: Flow<T>) :
    Flow<T> by innerFlow,
    DefaultFlow<T>


class SomeFeature {
    private val channel = Channel<Int>()

    val df: Flow<Int> = DefaultFlowImpl(3, channel.consumeAsFlow())

    fun doSome(number: Int) {
        GlobalScope.launch {
            channel.send(3)
        }
    }
}
