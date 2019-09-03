package com.lucianbc.receiptscan.presentation

import com.lucianbc.receiptscan.R.drawable.*
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.Category.*

val Category.icon
    get() = when(this) {
        Grocery -> ic_grocery
        Coffee -> ic_coffee_cup
        Transportation -> ic_taxi
        Snack -> ic_snack
        Restaurant -> ic_restaurant
        NotAssigned -> ic_plus
    }

val Category.displayName
    get() = when(this) {
        NotAssigned -> "Others"
        else -> name
    }