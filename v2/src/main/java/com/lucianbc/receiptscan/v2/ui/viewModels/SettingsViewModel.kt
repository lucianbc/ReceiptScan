package com.lucianbc.receiptscan.v2.ui.viewModels

import com.lucianbc.receiptscan.v2.domain.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface SettingsViewModel {
    val defaultCategory: StateFlow<Category>

    object Empty : SettingsViewModel {
        override val defaultCategory: StateFlow<Category> = MutableStateFlow(Category.Restaurant)
    }
}