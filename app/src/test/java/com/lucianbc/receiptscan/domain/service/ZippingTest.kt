package com.lucianbc.receiptscan.domain.service

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import org.junit.Test

class ZippingTest {
    @Test
    fun testZippingWithBehavior() {
        val fixed = PublishSubject.create<Long>()

        val continuous = PublishSubject.create<Int>()

        val zipped = Observable
            .combineLatest(fixed, continuous, BiFunction { t1: Long, t2: Int -> t1 to t2 })

        zipped.subscribe {
            println(it)
        }

        fixed.onNext(1L)

        continuous.onNext(12)
        continuous.onNext(13)

        Thread.sleep(1000)
    }
}