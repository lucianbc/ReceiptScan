package com.lucianbc.receiptscan.v2.ui.viewModels

import com.lucianbc.receiptscan.v2.domain.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

interface SettingsViewModel : CollectDataViewModel {
    val settingsState: StateFlow<ViewState>

    object Empty : SettingsViewModel {
        override val settingsState: StateFlow<ViewState> = MutableStateFlow(
            ViewState(
                Category.Restaurant,
                Currency.getInstance("RON"),
                true
            )
        )

        override fun toggleSendReceiptAnonymously() {}
    }

    data class ViewState(
        val defaultCategory: Category,
        val defaultCurrency: Currency,
        val shareAnonymousData: Boolean,
    )
}