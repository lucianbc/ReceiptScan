package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import io.reactivex.Flowable
import javax.inject.Inject

class ManageDraftUseCase(
    val draftId: Long,
    val value: Flowable<DraftWithProducts>
) {
    class Factory @Inject constructor(
        private val draftsRepository: DraftsRepository
    ) {
        fun fetch(draftId: Long): ManageDraftUseCase {
            val value = draftsRepository.getReceipt(draftId)
            return ManageDraftUseCase(draftId, value.cache())
        }
    }
}