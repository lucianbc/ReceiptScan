package com.lucianbc.receiptscan.v2.ui.viewModels

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.v2.domain.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class HomeViewModelImpl : ViewModel(),
    SettingsViewModel,
    CategoriesViewModel,
    CollectDataViewModel,
    CurrenciesViewModel {

    override val categories: StateFlow<List<Category>> =
        MutableStateFlow(Category.values().toList())

    private val _settingsState = MutableStateFlow(
        SettingsViewModel.ViewState(
            Category.NotAssigned,
            Currency.getInstance("RON"),
            false,
        )
    )
    
    override val settingsState: StateFlow<SettingsViewModel.ViewState> = _settingsState

    override fun setCategory(category: Category) {
        _settingsState.value = _settingsState.value.copy(defaultCategory = category)
    }

    override fun toggleSendReceiptAnonymously() {
        _settingsState.value =
            _settingsState.value.run { copy(shareAnonymousData = !shareAnonymousData) }
    }

    override val currencies: StateFlow<List<Currency>> =
        MutableStateFlow(Currency.getAvailableCurrencies().toList())

    override fun setCurrency(currency: Currency) {
        _settingsState.value = _settingsState.value.run { copy(defaultCurrency = currency) }
    }
}