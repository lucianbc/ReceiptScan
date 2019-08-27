package com.lucianbc.receiptscan.domain.drafts

import com.lucianbc.receiptscan.domain.extract.DraftId
import com.lucianbc.receiptscan.domain.collect.CollectingOption
import com.lucianbc.receiptscan.domain.collect.ReceiptCollector
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DraftsUseCaseImplTest {
    @Test
    fun `fetched manage multicasts the value and work is done once`() {

        val manage = subject.fetch(dummyId)

        // perform multiple downstream operations
        manage.value.map { it.merchantName }.test().awaitCount(1)
        manage.value.map { it.category }.test().awaitCount(1)
        manage.value.map { it.currency }.test().awaitCount(1)

        // check if work has been done only once
        assertEquals(1, count)
    }

    @Test
    fun `fetched use case has the expected value`() {
        val manage = subject.fetch(dummyId)

        manage.value.test()
            .awaitCount(1)
            .assertNotComplete()
            .assertValue(dummyDraft)
    }

    @Before
    fun setup() {
        val work = Flowable.fromCallable {
            count++
            dummyDraft
        }

        val mockRepo = mock<DraftsRepository> {
            on { getDraft(dummyId) } doReturn work
        }

        val mockShare = mock<CollectingOption>()
        val mockSender = mock<ReceiptCollector>()

        val mockFactory = object: ManageImpl.Factory {
            override fun create(draftId: DraftId, source: Flowable<Draft>): ManageImpl {
                return ManageImpl(draftId, source, mockRepo, mockShare, mockSender)
            }
        }

        subject = DraftsUseCaseImpl(mockRepo, mockFactory)
    }

    lateinit var subject: DraftsUseCase

    private var count = 0
}