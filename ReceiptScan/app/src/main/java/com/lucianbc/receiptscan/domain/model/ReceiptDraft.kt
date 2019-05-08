package com.lucianbc.receiptscan.domain.model

data class ReceiptDraft(
    val id: Int,
    val imagePath: String,
    val metadata: Collection<ScanInfoBox>
)

