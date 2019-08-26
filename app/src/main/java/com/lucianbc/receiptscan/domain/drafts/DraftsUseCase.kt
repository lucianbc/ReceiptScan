package com.lucianbc.receiptscan.domain.drafts

import io.reactivex.Flowable

interface DraftsUseCase {
    fun list(): Flowable<List<DraftListItem>>
}