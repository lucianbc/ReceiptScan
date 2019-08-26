package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.repository.AppRepository
import io.reactivex.Flowable
import javax.inject.Inject

class ListReceiptsUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    fun execute(): Flowable<List<Item>> =
            appRepository.getAllReceiptItems()

    data class Item(
        val id: Long,
        val merchantName: String,
        val total: Float
    )
}