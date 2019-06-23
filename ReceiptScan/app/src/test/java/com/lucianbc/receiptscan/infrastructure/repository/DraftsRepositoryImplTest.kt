package com.lucianbc.receiptscan.infrastructure.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.scanner.OcrElementValue
import com.lucianbc.receiptscan.infrastructure.dao.DraftDao
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.everyItem
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class DraftsRepositoryImplTest {

    @Test
    fun testCreate() {
        val subject = DraftsRepositoryImpl(draftDao, imagesDao)
        val obsResult = subject.create(draftValue)

        val subs = obsResult.test()
        subs
            .assertResult(draftId)
            .assertComplete()
        assertEquals(1, draftCaptor.allValues.size)
        assertNull(draftCaptor.firstValue.id)
        assertEquals(true, draftCaptor.firstValue.isDraft)
        assertEquals(filename, draftCaptor.firstValue.imagePath)

        assertEquals(1, annotationsCaptor.allValues.size)
        assertEquals(elements.toList().size, annotationsCaptor.firstValue.size)
        assertThat(annotationsCaptor.firstValue.map { it.receiptId }, everyItem(equalTo(draftId)))
    }


    private lateinit var draftDao: DraftDao
    private lateinit var imagesDao: ImagesDao
    private lateinit var image: Bitmap

    private lateinit var draftValue: DraftValue

    private val draftId = 1L

    private val filename = "filename"
    private val elements = sequenceOf(
        OcrElementValue("merchant", 1, 20, 30, 40),
        OcrElementValue("prod1", 35, 1, 45, 4),
        OcrElementValue("12.5 B", 35, 8, 45, 12)
    )

    private var draftCaptor = argumentCaptor<ReceiptEntity>()
    private var annotationsCaptor = argumentCaptor<List<OcrElement>>()

    @Before
    fun setup() {
        draftDao = mock()
        imagesDao = mock()
        image = mock()
        draftValue = DraftValue.fromOcrElementsAndImage(image to elements)
        `when`(imagesDao.saveImage(image)).thenReturn(filename)
        `when`(draftDao.insert(draftCaptor.capture())).thenReturn(Single.just(draftId))
        `when`(draftDao.insertProducts(any())).thenReturn(Single.just(emptyList()))
        `when`(draftDao.insert(annotationsCaptor.capture())).thenReturn(Single.just(listOf(1L, 2L)))
    }
}
