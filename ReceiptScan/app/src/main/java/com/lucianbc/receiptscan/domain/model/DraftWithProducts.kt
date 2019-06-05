package com.lucianbc.receiptscan.domain.model

import androidx.room.Embedded
import androidx.room.Relation

class ReceiptDraftWithProducts (
    @Embedded
    val receipt: ReceiptDraft,

    @Relation(parentColumn = "id", entityColumn = "draftId")
    val products: List<ProductDraft>
)