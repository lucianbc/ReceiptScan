package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import java.util.*

class ManageDraftUseCaseTest {
    // region Setup
    private val id = 1L
    private val data = DraftWithProducts(
        Draft(
            "image",
            "merchant",
            Date(),
            13F,
            Currency.getInstance("RON"),
            Date(),
            id
        ),
        listOf(
            Product("prod1", 12F),
            Product("prod2", 1F)
        )
    )

    private val repo: DraftsRepository = mock()

    private val subject = ManageDraftUseCase(id, Flowable.just(data), repo)

    private val savedValueCaptor = argumentCaptor<Draft>()

    @Before
    fun setup() {
        `when`(repo.update(savedValueCaptor.capture())).thenReturn(Single.just(id))
    }
    // endregion

    @Test
    fun testValueChanged() {
        val merchant = "NEW MERCHANT"
        subject.update(merchant) { v, dwp -> dwp.receipt.copy(merchantName = v) }

        Thread.sleep(500)
        verify(repo, times(1)).insert(any())
        assertEquals(merchant, savedValueCaptor.firstValue.merchantName)
        verifyNoMoreInteractions(repo)
    }

    @Test
    fun testSameValueNotUpdate() {
        val sameMerchant = String(data.receipt.merchantName!!.toCharArray())
        subject.update(sameMerchant) { v, dwp -> dwp.receipt.copy(merchantName = v) }

        Thread.sleep(500)
        verifyZeroInteractions(repo)
    }
}