package com.lucianbc.receiptscan.domain.model

data class ScanInfoBox(
    val top: Int,
    val bottom: Int,
    val width: Int,
    val height: Int,
    val value: String
)

typealias ScanInfo = Collection<ScanInfoBox>