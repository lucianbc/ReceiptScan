package com.lucianbc.receiptscan.domain.viewfinder

import io.reactivex.Observable
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class LiveViewUseCaseTest {

    @Test
    fun `when fast scans the ocr elements are produced at given fps`() {
        // GIVEN
        val fps = 15f
        val sec = 1

        // WHEN
        var count = 0f
        val useCase = LiveViewUseCase(fps)
        useCase.ocrElements.subscribe {
            count += 1
        }

        val subscription = Observable
            .interval(0, 2, TimeUnit.MILLISECONDS)
            .subscribe {
                useCase.scanFrame(dummyFrame)
            }
        Thread.sleep(sec * 1000L)
        subscription.dispose()


        // THEN
        assertEquals(fps, count)
    }

    private val dummyFrame = object : FrameProducer {
        override fun produce(): Observable<OcrElements> {
            return Observable.just(sequenceOf(
                OcrElementValue(
                    "text",
                    0,
                    0,
                    1,
                    1
                )
            ))
        }
    }
}