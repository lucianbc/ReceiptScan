package com.lucianbc.receiptscan.domain.extract

import com.lucianbc.receiptscan.domain.viewfinder.OcrElements
import com.lucianbc.receiptscan.domain.viewfinder.Scannable
import io.reactivex.Flowable
import io.reactivex.Single

interface ExtractUseCase {
    val state: Flowable<State>
    val preview: Flowable<OcrElements>
    fun fetchPreview(frame: Scannable)
    fun extract(frame: Scannable): Single<DraftId>
}