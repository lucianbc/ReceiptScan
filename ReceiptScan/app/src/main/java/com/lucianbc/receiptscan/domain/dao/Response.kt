package com.lucianbc.receiptscan.domain.dao

import androidx.lifecycle.toLiveData
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

typealias Response<T> = Flowable<T>

fun <T>Response<T>.ioLiveData() =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .toLiveData()
