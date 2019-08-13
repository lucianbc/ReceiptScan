package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.Category
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import java.util.*

class ManageReceiptUseCaseTest {

    private lateinit var subject: ManageReceiptUseCase
    private val source = PublishSubject.create<ManageReceiptUseCase.Value>()
    private lateinit var value: ManageReceiptUseCase.Value

    @Before
    fun setup() {
        value = ManageReceiptUseCase.Value(
            1L,
            "Merchant",
            Date(),
            2F,
            Currency.getInstance("RON"),
            Category.Grocery,
            emptyList()
        )
        subject = ManageReceiptUseCase(source.toFlowable(BackpressureStrategy.LATEST))
    }

    @Test
    fun `given useCase with connected receipt when exportReceipt then subscriber is called`() {
        subject.receipt.subscribe { println("Connected receipt") }
        source.onNext(value)

        subject.exportReceipt().test().assertComplete()
    }
}