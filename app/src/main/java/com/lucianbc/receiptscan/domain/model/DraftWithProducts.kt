package com.lucianbc.receiptscan.domain.model

import androidx.room.Embedded
import androidx.room.Relation

class DraftWithProducts(
    @Embedded
    val receipt: Draft,

    @Relation(parentColumn = "id", entityColumn = "receiptId")
    val products: List<Product>
)