package com.lucianbc.receiptscan.domain.viewfinder

import android.graphics.Bitmap
import io.reactivex.Observable

interface Scannable {
    fun ocrElements(): Observable<OcrElements>
    fun image(): Observable<Bitmap>
}