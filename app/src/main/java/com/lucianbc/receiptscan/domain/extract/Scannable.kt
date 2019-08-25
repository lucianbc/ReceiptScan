package com.lucianbc.receiptscan.domain.extract

import android.graphics.Bitmap
import io.reactivex.Observable

interface Scannable {
    fun ocrElements(): Observable<OcrElements>
    fun image(): Observable<Bitmap>
}