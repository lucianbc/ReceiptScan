package com.lucianbc.receiptscan.domain.model

import android.graphics.Bitmap

interface CreateDraftCommand {
    val image: Bitmap
    val elements: OcrElements
}

inline class Command(private val data: Pair<Bitmap, OcrElements>)
    : CreateDraftCommand {
    override val elements: OcrElements
        get() = data.second
    override val image: Bitmap
        get() = data.first
}
