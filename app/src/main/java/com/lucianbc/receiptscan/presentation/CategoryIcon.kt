package com.lucianbc.receiptscan.presentation

import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.model.Category

val Category.icon
    get() = when(this) {
        Category.Grocery -> R.drawable.ic_grocery
        Category.Coffee -> R.drawable.ic_coffee_cup
        Category.Transportation -> R.drawable.ic_taxi
        Category.Snack -> R.drawable.ic_snack
    }