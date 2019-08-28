package com.lucianbc.receiptscan.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

fun <T> throttled(
    disposable: CompositeDisposable,
    timeout: Long,
    unit: TimeUnit,
    func: ((T) -> Unit)
): ((T) -> Unit) {
    val subject = PublishSubject.create<T>()

    subject
        .throttleLast(timeout, unit)
        .subscribe(func)
        .addTo(disposable)

    return { t -> subject.onNext(t) }
}
