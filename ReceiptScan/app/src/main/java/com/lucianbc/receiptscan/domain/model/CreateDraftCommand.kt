package com.lucianbc.receiptscan.domain.model

import android.graphics.Bitmap

interface CreateDraftCommand {
    val image: Bitmap
    val annotations: Annotations
}

inline class Command(private val data: Pair<Bitmap, Annotations>)
    : CreateDraftCommand {
    override val annotations: Annotations
        get() = data.second
    override val image: Bitmap
        get() = data.first
}
