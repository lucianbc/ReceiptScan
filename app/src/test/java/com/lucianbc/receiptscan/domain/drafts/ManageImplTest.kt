package com.lucianbc.receiptscan.domain.drafts

import com.lucianbc.receiptscan.domain.model.SharingOption
import com.lucianbc.receiptscan.domain.service.ReceiptSender
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ManageImplTest {

    @Test
    fun `when update value changes`() {
        val merchant = "NEW MERCHANT"
        subject
            .update(merchant)
            { v, dwp -> dwp.copy(merchantName = v) }
            .test()
            .await()
            .assertComplete()

        verify(mockRepo, times(1))
            .update(any())
        assertEquals(merchant, savedValueCaptor.firstValue.merchantName)
        verifyNoMoreInteractions(mockRepo)
    }

    @Test
    fun `when updating the same value no update is fired to the repository`() {
        val sameMerchant = String(dummyDraft.merchantName!!.toCharArray())
        subject
            .update(sameMerchant)
            { v, dwp -> dwp.copy(merchantName = v) }
            .test()
            .await()
            .assertComplete()

        verifyNoMoreInteractions(mockRepo)
    }

    @Test
    fun `validate triggers send receipt when enabled`() {
        mockSharingOption.isEnabled = true
        subject.moveToValid().test()
            .await().assertComplete()
        verify(mockSender, times(1)).send(dummyId)
        verifyNoMoreInteractions(mockSender)
    }

    @Test
    fun `validate does not trigger send receipt when not enabled`() {
        mockSharingOption.isEnabled = false
        subject.moveToValid().test().await().assertComplete()
        verifyZeroInteractions(mockSender)
    }

    @Test
    fun `send receipt is called accordingly when changing enabled`() {
        mockSharingOption.isEnabled = false
        subject.moveToValid().test().await().assertComplete()
        verifyZeroInteractions(mockSender)

        mockSharingOption.isEnabled = true
        subject.moveToValid().test().await().assertComplete()
        verify(mockSender, times(1)).send(dummyId)

        mockSharingOption.isEnabled = false
        subject.moveToValid().test().await().assertComplete()
        verifyNoMoreInteractions(mockSender)
    }

    // region Setup

    @Before
    fun setup() {
        whenever(mockRepo.update(savedValueCaptor.capture())).thenReturn(Completable.complete())
        whenever(mockRepo.moveDraftToValid(dummyId)).thenReturn(Completable.complete())
    }

    private val mockSharingOption = object : SharingOption {
        var isEnabled = false
        override val enabled: Boolean
            get() = isEnabled
        override val appId = "myApp"
    }

    private val mockSender = mock<ReceiptSender>()

    private val mockRepo = mock<DraftsRepository>()

    private val savedValueCaptor = argumentCaptor<Draft>()

    private val subject: DraftsUseCase.Manage = ManageImpl(
        dummyId,
        Flowable.just(dummyDraft).replay(1).autoConnect(),
        mockRepo,
        mockSharingOption,
        mockSender
    )
    // endregion
}