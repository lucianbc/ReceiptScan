package com.lucianbc.receiptscan.domain.repository

import com.lucianbc.receiptscan.domain.model.CreateDraftCommand
import com.lucianbc.receiptscan.domain.model.DraftItem
import io.reactivex.Flowable
import io.reactivex.Observable

interface DraftsRepository {
    fun create(command: CreateDraftCommand): Observable<Long>
    fun getAllItems(): Flowable<List<DraftItem>>
}