package com.lucianbc.receiptscan.domain.service

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.OcrElements
import io.reactivex.Observable

interface OcrElementsProducer {
    fun produce(): Observable<OcrElements>
}

interface OcrWithImageProducer {
    fun produce(): Observable<Pair<Bitmap, OcrElements>>
}