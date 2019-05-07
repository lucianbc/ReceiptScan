package com.lucianbc.receiptscan.util

sealed class Optional<out T: Any>

data class Just<T: Any>(val value: T) : Optional<T>()

object None : Optional<Nothing>()

fun <T : Any> T?.lift(): Optional<T> = if (this == null) None else Just(this)

fun <T: Any, R: Any> Optional<T>.fmap(func: (T) -> Optional<R>): Optional<R> {
    return when(this) {
        is Just -> func(this.value)
        else -> None
    }
}