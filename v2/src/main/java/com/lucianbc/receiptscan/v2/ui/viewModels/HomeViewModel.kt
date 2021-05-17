package com.lucianbc.receiptscan.v2.ui.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.v2.ui.screens.Category
import com.lucianbc.receiptscan.v2.ui.screens.SettingsViewModel

class HomeViewModelImpl : ViewModel(), SettingsViewModel {
    private val categoryMutable = mutableStateOf(Category.Restaurant)

    override val defaultCategory: State<Category> = categoryMutable

    override fun updateCategory(category: Category) {
        categoryMutable.value = category
    }
}
