package com.lucianbc.receiptscan.domain.scanner

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.rxkotlin.mergeAll
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

typealias OcrElements = Sequence<OcrElementValue>

class LiveViewUseCase(fps: Float) {
    private val frameRate = (1000 / fps).toLong()

    private val frameSource = PublishSubject.create<Frame>()

    val ocrElements: Flowable<OcrElements> =
            frameSource
                .mergeAll()
                .throttleLast(frameRate, FRAME_UNIT)
                .toFlowable(BackpressureStrategy.LATEST)

    fun scanFrame(frame: FrameProducer) {
        frameSource.onNext(frame.produce())
    }

    companion object {
        private val FRAME_UNIT = TimeUnit.MILLISECONDS
    }
}