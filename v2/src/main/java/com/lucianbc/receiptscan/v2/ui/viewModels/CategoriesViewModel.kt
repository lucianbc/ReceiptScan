package com.lucianbc.receiptscan.v2.ui.viewModels

import com.lucianbc.receiptscan.v2.domain.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface CategoriesViewModel {
    val categories: StateFlow<List<Category>>
    fun setCategory(category: Category)

    object Empty : CategoriesViewModel {
        override val categories: StateFlow<List<Category>> =
            MutableStateFlow(Category.values().toList())

        override fun setCategory(category: Category) {}
    }
}