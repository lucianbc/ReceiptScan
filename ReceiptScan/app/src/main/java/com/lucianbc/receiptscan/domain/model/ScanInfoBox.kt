package com.lucianbc.receiptscan.domain.model

data class ScanInfoBox(
    val top: Int,
    val bottom: Int,
    val left: Int,
    val right: Int,
    val value: String
)

typealias ScanAnnotations = Collection<ScanInfoBox>