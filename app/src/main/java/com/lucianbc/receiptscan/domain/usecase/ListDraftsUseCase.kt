package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import java.util.*
import javax.inject.Inject

class ListDraftsUseCase @Inject constructor(
    private val draftsRepository: DraftsRepository
) {
    fun execute() = draftsRepository.getAllItems()

    data class DraftItem(
        val id: Long,
        val creationTimestamp: Date
    )
}