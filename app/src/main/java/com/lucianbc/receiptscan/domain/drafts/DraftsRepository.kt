package com.lucianbc.receiptscan.domain.drafts

import io.reactivex.Flowable

interface DraftsRepository {
    fun listDrafts(): Flowable<List<DraftListItem>>
}