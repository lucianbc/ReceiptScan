package com.lucianbc.receiptscan.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

fun <T> debounced(
    disposable: CompositeDisposable,
    timeout: Long,
    unit: TimeUnit,
    func: ((T) -> Unit)
): ((T) -> Unit) {
    val subject = PublishSubject.create<T>()

    val d = subject
        .throttleLast(timeout, unit)
        .subscribe(func)

    disposable.add(d)

    return { t -> subject.onNext(t) }
}