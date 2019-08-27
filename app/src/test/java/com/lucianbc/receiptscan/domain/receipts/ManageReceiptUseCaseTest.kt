package com.lucianbc.receiptscan.domain.receipts

import com.lucianbc.receiptscan.domain.model.Category
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import java.util.*

class ManageReceiptUseCaseTest {

    private lateinit var subject: ManageReceiptUseCase
    private val source = PublishSubject.create<Receipt>()
    private lateinit var value: Receipt

    @Before
    fun setup() {
        value = Receipt(
            1L,
            "Merchant",
            Date(),
            2F,
            Currency.getInstance("RON"),
            Category.Grocery,
            "path",
            emptyList()
        )
        subject = ManageReceiptUseCase(
            source.toFlowable(BackpressureStrategy.LATEST)
        )
    }

    @Test
    fun `given useCase with connected receipt when exportReceipt then subscriber completes`() {
        subject.receipt.subscribe()
        source.onNext(value)

        subject.exportReceipt().test().await().assertComplete()
    }

    @Test
    fun `given useCase with connected receipt when exportPath then subscriber completes`() {
        subject.receipt.subscribe()
        source.onNext(value)

        subject.exportPath().test().assertComplete()
    }
}