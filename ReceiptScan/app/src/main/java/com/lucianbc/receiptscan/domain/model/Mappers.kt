package com.lucianbc.receiptscan.domain.model

fun OcrElement.toAnnotation(tag: Tag): Annotation =
    Annotation(this.text, this.top, this.bottom, this.left, this.right, tag)