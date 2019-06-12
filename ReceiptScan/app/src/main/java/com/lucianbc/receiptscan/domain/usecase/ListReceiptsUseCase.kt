package com.lucianbc.receiptscan.domain.usecase

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class ListReceiptsUseCase @Inject constructor() {

    fun execute(): Flowable<List<Item>>
            = Observable.just(items).toFlowable(BackpressureStrategy.LATEST)

    private val items = listOf(
        Item(1, "Lidl", 12.3F),
        Item(2, "Sc Profi SRL", 11.99F),
        Item(3, "Auchan", 112F)
    )

    data class Item (
        val id: Long,
        val merchant: String,
        val price: Float
    )
}