package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import io.reactivex.Flowable
import javax.inject.Inject

class ListReceiptsUseCase @Inject constructor(
    private val draftsRepository: DraftsRepository
) {
    fun execute(): Flowable<List<Item>> =
            draftsRepository.getAllReceiptItems()

    data class Item(
        val id: Long,
        val merchantName: String,
        val total: Float
    )
}