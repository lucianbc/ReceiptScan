package com.lucianbc.receiptscan.domain.scanner

import android.graphics.Bitmap
import io.reactivex.Observable

typealias Frame = Observable<OcrElements>
typealias ImageFrame = Observable<Pair<Bitmap, OcrElements>>

interface FrameProducer {
    fun produce(): Frame
}

interface OcrWithImageProducer {
    fun produce(): ImageFrame
}