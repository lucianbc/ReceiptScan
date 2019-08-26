package com.lucianbc.receiptscan.infrastructure

import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.lucianbc.receiptscan.domain.extract.OcrElement

fun FirebaseVisionText.Line.toOcrElement(): OcrElement? {
    return this.boundingBox ?.let {
        OcrElement(
            this.text,
            it.top,
            it.bottom,
            it.left,
            it.right
        )
    }
}
