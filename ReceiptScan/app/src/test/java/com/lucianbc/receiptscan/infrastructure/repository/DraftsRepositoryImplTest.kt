package com.lucianbc.receiptscan.infrastructure.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.infrastructure.dao.DraftDao
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
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
        val obsResult = subject.create(cmd)
        val subs = obsResult.test()
        subs
            .assertResult(draftId)
            .assertComplete()
        assertEquals(1, draftCaptor.allValues.size)
        assertNull(draftCaptor.firstValue.id)
        assertEquals(true, draftCaptor.firstValue.isLinked)
        assertEquals(filename, draftCaptor.firstValue.imagePath)

        assertEquals(1, annotationsCaptor.allValues.size)
        assertEquals(annotations.toList().size, annotationsCaptor.firstValue.size)
        assertThat(annotationsCaptor.firstValue.map { it.draftId }, everyItem(equalTo(draftId)))
    }


    private lateinit var draftDao: DraftDao
    private lateinit var imagesDao: ImagesDao
    private lateinit var image: Bitmap

    private lateinit var cmd: CreateDraftCommand

    private val filename = "filename"
    private val annotations = sequenceOf(
        Annotation("an1", 1, 2, 3, 4, Tag.Noise),
        Annotation("an2", 2, 4, 6, 8, Tag.Date)
    )
    private val draftId = 1L

    private var draftCaptor = argumentCaptor<Draft>()
    private var annotationsCaptor = argumentCaptor<List<Annotation>>()

    @Before
    fun setup() {
        draftDao = mock()
        imagesDao = mock()
        image = mock()
        cmd = Command(image to annotations)
        `when`(imagesDao.saveImage(image)).thenReturn(filename)
        `when`(draftDao.insert(draftCaptor.capture())).thenReturn(Single.just(draftId))
        `when`(draftDao.insert(annotationsCaptor.capture())).thenReturn(Single.just(listOf(1L, 2L)))
    }
}
