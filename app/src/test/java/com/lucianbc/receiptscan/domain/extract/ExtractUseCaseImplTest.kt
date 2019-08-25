package com.lucianbc.receiptscan.domain.extract

import android.graphics.Bitmap
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import java.util.concurrent.TimeUnit

class ExtractUseCaseImplTest {

    @Test
    fun `use case state is idle and not completed when instantiated`() {
        subject = ExtractUseCaseImpl(mockFps, mockRepo, mockExtractor)
        subject.state.test()
            .assertNoErrors()
            .assertValue(State.Idle)
            .assertNotComplete()
    }

    @Test
    fun `when processing use case goes through all states and does not complete`() {
        `when`(mockRepo.persist(any(), any())).thenReturn(Single.just(dummyDraftId))
        `when`(mockExtractor.invoke(dummyElements)).thenReturn(mock())

        subject = ExtractUseCaseImpl(mockFps, mockRepo, mockExtractor)

        val observer = subject.state.test()

        subject.extract(dummyScannable).test().await().assertComplete()

        observer.assertNoErrors()
            .assertValues(State.Idle, State.Processing, State.Idle)
            .assertNotComplete()
    }

    @Test
    fun `when exception use case goes to error`() {
        val ex = Exception()

        `when`(mockRepo.persist(any(), any())).thenReturn(Single.error(ex))
        `when`(mockExtractor.invoke(dummyElements)).thenReturn(mock())

        subject = ExtractUseCaseImpl(mockFps, mockRepo, mockExtractor)

        val observer = subject.state.test()

        subject.extract(dummyScannable).test().await().assertError(Exception::class.java)

        observer.assertNoErrors()
            .assertValues(State.Idle, State.Processing, State.Error(ex))
            .assertNotComplete()
    }

    @Test
    fun `when extract, the draft id is returned`() {
        `when`(mockRepo.persist(any(), any())).thenReturn(Single.just(dummyDraftId))
        `when`(mockExtractor.invoke(dummyElements)).thenReturn(mock())

        subject = ExtractUseCaseImpl(mockFps, mockRepo, mockExtractor)

        subject.extract(dummyScannable).test()
            .await()
            .assertComplete()
            .assertValue(dummyDraftId)
    }

    @Test
    fun `when fast scans the ocr elements are produced at given fps`() {
        // GIVEN
        val fps = 15f
        val sec = 1L

        // WHEN
        var count = 0f
        subject = ExtractUseCaseImpl(fps, mockRepo, mockExtractor)
        subject.preview.subscribe {
            count += 1
        }

        val source = Observable
            .interval(50, 2, TimeUnit.MILLISECONDS)
            .subscribe {
                subject.fetchPreview(dummyScannable)
            }

        Thread.sleep(TimeUnit.SECONDS.toMillis(sec))
        source.dispose()

        // THEN
        assertEquals(fps, count)
    }

    private lateinit var subject: ExtractUseCase

    private val mockFps = 15f
    private val mockRepo : ExtractRepository = mock()
    private val mockExtractor: Extractor = mock()

    private val dummyDraftId = 1L
    private val dummyElements = sequenceOf(OcrElement("text", 0, 0, 1, 1))

    private val dummyScannable = object : Scannable {
        override fun ocrElements(): Observable<OcrElements> {
            return Observable.just(dummyElements)
        }

        override fun image(): Observable<Bitmap> {
            return Observable.just(mock())
        }
    }
}