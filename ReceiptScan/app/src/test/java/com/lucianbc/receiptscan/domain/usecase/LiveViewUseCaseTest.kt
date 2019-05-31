package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.OcrElement
import com.lucianbc.receiptscan.domain.service.OcrElements
import com.lucianbc.receiptscan.domain.service.OcrElementsProducer
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class LiveViewUseCaseTest {

    @Test
    fun testFrameRate() {
        val fps = 15
        val sec = 1

        var count = 0
        val useCase = LiveViewUseCase(fps)
        useCase.ocrElements.subscribe {
            count += 1
        }

        val subscription = Observable
            .interval(0, 2, TimeUnit.MILLISECONDS)
            .subscribe {
                useCase.scanFrame(frame)
            }
        Thread.sleep(sec * 1000L)
        subscription.dispose()

        assertEquals(fps, count)
    }

    private val frame = object : OcrElementsProducer {
        override fun produce(): Observable<OcrElements> {
            return Observable.just(sequenceOf(OcrElement("text", 0, 0, 1, 1)))
        }
    }
}