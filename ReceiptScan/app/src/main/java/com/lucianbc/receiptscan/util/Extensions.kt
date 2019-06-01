package com.lucianbc.receiptscan.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <X, Y> LiveData<X>.map(func: (X) -> Y): LiveData<Y> =
    Transformations.map(this, func)