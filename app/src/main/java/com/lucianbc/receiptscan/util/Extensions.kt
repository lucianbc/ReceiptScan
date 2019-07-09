package com.lucianbc.receiptscan.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import java.util.*

fun <X, Y> LiveData<X>.map(func: (X) -> Y): LiveData<Y> =
    Transformations.map(this, func)

fun Long.toDate() = Date(this)

fun <T : Any?> T.guard(cond: Boolean, func: (T) -> Unit) {
    if (cond) {
        func.invoke(this)
    }
}