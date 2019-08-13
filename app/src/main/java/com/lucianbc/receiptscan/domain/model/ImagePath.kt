package com.lucianbc.receiptscan.domain.model

import android.graphics.Bitmap
import java.io.File
import java.util.*

inline class ImagePath(val value: String) {
    override fun toString(): String = value

    companion object ImagePath {
        fun random() = ImagePath("scan_${ UUID.randomUUID()}.${ FORMAT.name}")
        val FORMAT = Bitmap.CompressFormat.JPEG
    }
}

fun ImagePath.file(base: File) = File(base, value)