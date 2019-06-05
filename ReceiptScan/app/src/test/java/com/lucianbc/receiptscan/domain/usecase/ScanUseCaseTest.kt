package com.lucianbc.receiptscan.domain.usecase

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.OcrWithImageProducer
import com.lucianbc.receiptscan.domain.service.TaggingService
import com.nhaarman.mockitokotlin2.*
import org.mockito.Mockito.`when`
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ScanUseCaseTest {

    @Test
    fun testIdleAtFirst() {
        val subject = ScanUseCase(taggingServiceMock, draftRepoMock)
        val crtState = subject.state.blockingFirst()
        assertEquals(ScanUseCase.State.Idle, crtState)
    }

    @Test
    fun testGoingThroughAllStates() {
        val subject = ScanUseCase(taggingServiceMock, draftRepoMock)
        val states = mutableListOf<ScanUseCase.State>()

        subject.state.subscribe { states.add(it) }
        subject.scan(parameter).subscribe()

        val expectedStates = listOf(
            ScanUseCase.State.Idle,
            ScanUseCase.State.OCR,
            ScanUseCase.State.Tagging,
            ScanUseCase.State.Saving,
            ScanUseCase.State.Idle
        )

        Thread.sleep(500)

        assertEquals(expectedStates, states)
        verify(taggingServiceMock, times(1)).tag(argElements)
        verify(draftRepoMock, times(1)).create(any())
    }

    private lateinit var argBitmap: Bitmap
    private lateinit var argElements: Sequence<OcrElement>
    private lateinit var parameter: OcrWithImageProducer

    private lateinit var taggingServiceMock: TaggingService

    private lateinit var draftRepoMock: DraftsRepository

    @Before
    fun setup() {
        draftRepoMock = mock()
        `when`(draftRepoMock.create(any())).thenReturn(Observable.just(1L))

        taggingServiceMock = mock()
        `when`(taggingServiceMock.tag(any())).then {
            val arg = (it.getArgument(0) as OcrElements)
            arg.map { e -> e.toAnnotation(Tag.Noise) }
        }
        argBitmap = mock()
        argElements = sequenceOf(OcrElement("text", 0, 0, 1, 1))

        parameter = object: OcrWithImageProducer {
            override fun produce(): Observable<Pair<Bitmap, OcrElements>> =
                Observable.just(argBitmap to argElements)
        }
    }

    @After
    fun after() {
        verifyNoMoreInteractions(taggingServiceMock)
        verifyNoMoreInteractions(draftRepoMock)
    }
}