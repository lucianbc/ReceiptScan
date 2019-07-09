package com.lucianbc.receiptscan.domain.viewfinder

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

typealias OcrElements = Sequence<OcrElementValue>

class LiveViewUseCase(fps: Float) {
    private val frameRate = (1000 / fps).toLong()

    private val frameSource = BehaviorSubject.create<Scannable>()

    val ocrElements: Flowable<OcrElements> =
        frameSource
            .toFlowable(BackpressureStrategy.LATEST)
            .observeOn(Schedulers.computation(), false, 1)
            .flatMap {
                it
                    .ocrElements()
                    .onErrorResumeNext(Observable.empty())
                    .toFlowable(BackpressureStrategy.LATEST)
            }
            .onBackpressureLatest()
            .throttleLast(frameRate, FRAME_UNIT)

    fun scan(frame: Scannable) =
        frameSource.onNext(frame)

    companion object {
        private val FRAME_UNIT = TimeUnit.MILLISECONDS
    }
}