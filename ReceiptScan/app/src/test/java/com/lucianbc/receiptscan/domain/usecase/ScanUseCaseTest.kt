package com.lucianbc.receiptscan.domain.usecase

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.OcrElementValue
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.scanner.OcrElements
import com.lucianbc.receiptscan.domain.scanner.OcrWithImageProducer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class ScanUseCaseTest {

    @Test
    fun testIdleAtFirst() {
        val subject = ScanUseCase(draftRepoMock)
        val crtState = subject.state.blockingFirst()
        assertEquals(ScanUseCase.State.Idle, crtState)
    }

    @Test
    fun testGoingThroughAllStates() {
        val subject = ScanUseCase(draftRepoMock)
        val states = mutableListOf<ScanUseCase.State>()

        subject.state.subscribe { states.add(it) }
        subject.scan(parameter).subscribe()

        val expectedStates = listOf(
            ScanUseCase.State.Idle,
            ScanUseCase.State.OCR,
            ScanUseCase.State.Saving,
            ScanUseCase.State.Idle
        )

        Thread.sleep(500)

        assertEquals(expectedStates, states)
        verify(draftRepoMock, times(1)).create(any())
    }

    private lateinit var argBitmap: Bitmap
    private lateinit var argElements: Sequence<OcrElementValue>
    private lateinit var parameter: OcrWithImageProducer

    private lateinit var draftRepoMock: DraftsRepository

    @Before
    fun setup() {
        draftRepoMock = mock()
        `when`(draftRepoMock.create(any())).thenReturn(Observable.just(1L))

        argBitmap = mock()
        argElements = sequenceOf(OcrElementValue("text", 0, 0, 1, 1))

        parameter = object: OcrWithImageProducer {
            override fun produce(): Observable<Pair<Bitmap, OcrElements>> =
                Observable.just(argBitmap to argElements)
        }
    }

    @After
    fun after() {
        verifyNoMoreInteractions(draftRepoMock)
    }
}