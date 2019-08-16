package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.ReceiptSender
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Flowable
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
            Category.Grocery,
            Date(),
            id
        ),
        listOf(
            Product("prod1", 12F),
            Product("prod2", 1F)
        )
    )

    private val sharingOption = object: SharingOption {
        var isEnabled = false
        override val enabled
            get() = isEnabled
    }

    private val sender: ReceiptSender = mock()

    private val repo: DraftsRepository = mock()

    private val subject = ManageDraftUseCase(id,
        Flowable.just(data).replay(1).autoConnect(),
        repo,
        sharingOption,
        sender)

    private val savedValueCaptor = argumentCaptor<Draft>()

    @Before
    fun setup() {
        `when`(repo.update(savedValueCaptor.capture())).thenReturn(Completable.complete())
    }
    // endregion

    @Test
    fun testValueChanged() {
        val merchant = "NEW MERCHANT"
        subject.update(merchant) { v, dwp -> dwp.receipt.copy(merchantName = v) }

        Thread.sleep(500)
        verify(repo, times(1)).update(any<Draft>())
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

    @Test
    fun `send receipt being called when enabled`() {
        sharingOption.isEnabled = true
        subject.validateDraft().test().assertComplete()
        verify(sender, times(1)).send(id)
        verifyNoMoreInteractions(sender)
    }

    @Test
    fun `send receipt is not called when not enabled`() {
        sharingOption.isEnabled = false
        subject.validateDraft().test().assertComplete()
        verifyZeroInteractions(sender)
    }

    @Test
    fun `send receipt is called accordingly when changing enabled`() {
        sharingOption.isEnabled = false
        subject.validateDraft().test().assertComplete()
        verifyZeroInteractions(sender)

        sharingOption.isEnabled = true
        subject.validateDraft().test().assertComplete()
        verify(sender, times(1)).send(id)

        sharingOption.isEnabled = false
        subject.validateDraft().test().assertComplete()
        verifyNoMoreInteractions(sender)
    }
}