package com.lucianbc.receiptscan.domain.extract

import io.reactivex.Flowable
import io.reactivex.Single

interface ExtractUseCase {
    val state: Flowable<State>
    val preview: Flowable<OcrElements>
    fun feedPreview(frame: Scannable)
    fun extract(frame: Scannable): Single<DraftId>
}