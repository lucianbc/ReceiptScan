package com.lucianbc.receiptscan.domain.repository

import com.lucianbc.receiptscan.domain.model.CreateDraftCommand
import io.reactivex.Observable

interface DraftsRepository {
    fun create(command: CreateDraftCommand): Observable<Long>
}