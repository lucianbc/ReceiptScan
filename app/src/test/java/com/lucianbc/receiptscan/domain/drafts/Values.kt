package com.lucianbc.receiptscan.domain.drafts

import com.lucianbc.receiptscan.domain.model.Category
import java.util.*

const val dummyId = 1L
val dummyDraft = Draft(
    dummyId,
    "merchant",
    Date(),
    2F,
    Currency.getInstance("RON"),
    Category.Grocery,
    listOf(
        Product("prod1", 1F, 1),
        Product("prod2", 1F, 2)
    ),
    listOf()
)