package com.lucianbc.receiptscan.v2.ui.viewModels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

interface CurrenciesViewModel {
    val currencies: StateFlow<List<Currency>>
    fun setCurrency(currency: Currency)

    object Empty : CurrenciesViewModel {
        override val currencies: StateFlow<List<Currency>> =
            MutableStateFlow(Currency.getAvailableCurrencies().toList())

        override fun setCurrency(currency: Currency) {}
    }
}