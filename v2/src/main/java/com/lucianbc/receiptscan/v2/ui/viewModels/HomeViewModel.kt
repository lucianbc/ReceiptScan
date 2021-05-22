package com.lucianbc.receiptscan.v2.ui.viewModels

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.v2.domain.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModelImpl : ViewModel(), SettingsViewModel, CategoriesViewModel {
    private val _defaultCategory = MutableStateFlow(Category.NotAssigned)

    override val defaultCategory = _defaultCategory

    override val categories: StateFlow<List<Category>> =
        MutableStateFlow(Category.values().toList())

    override fun setCategory(category: Category) {
        _defaultCategory.value = category
    }
}
