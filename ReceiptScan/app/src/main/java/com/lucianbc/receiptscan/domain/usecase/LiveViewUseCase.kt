package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.OcrElements
import com.lucianbc.receiptscan.domain.service.OcrElementsProducer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.rxkotlin.mergeAll
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class LiveViewUseCase(fps: Float) {
    private val frameRate = (MULTIPLE / fps).toLong()

    private val frameProducer = PublishSubject.create<Observable<OcrElements>>()

    val ocrElements: Flowable<OcrElements> =
            frameProducer
                .mergeAll()
                .throttleLast(frameRate, FRAME_UNIT)
                .toFlowable(BackpressureStrategy.LATEST)

    fun scanFrame(frame: OcrElementsProducer) {
        frameProducer.onNext(frame.produce())
    }

    companion object {
        private val FRAME_UNIT = TimeUnit.MILLISECONDS
        private const val MULTIPLE = 1000
    }
}