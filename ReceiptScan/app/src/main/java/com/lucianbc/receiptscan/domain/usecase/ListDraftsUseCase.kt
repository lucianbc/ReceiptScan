package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import javax.inject.Inject

class ListDraftsUseCase @Inject constructor(
    private val draftsRepository: DraftsRepository
) {
    fun execute() = draftsRepository.getAllItems()
}