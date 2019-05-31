package com.lucianbc.receiptscan.domain.service

import com.lucianbc.receiptscan.domain.model.OcrElement
import io.reactivex.Observable

typealias OcrElements = Sequence<OcrElement>

interface OcrElementsProducer {
    fun produce(): Observable<OcrElements>
}