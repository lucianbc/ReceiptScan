package com.lucianbc.receiptscan.domain.model

data class OcrElement (
    val text: String,
    val top: Int,
    val left: Int,
    val bottom: Int,
    val right: Int
)