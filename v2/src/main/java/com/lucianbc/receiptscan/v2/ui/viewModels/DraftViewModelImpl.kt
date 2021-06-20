package com.lucianbc.receiptscan.v2.ui.viewModels

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.v2.domain.Category
import com.lucianbc.receiptscan.v2.ui.screens.Receipt
import com.lucianbc.receiptscan.v2.ui.screens.ReceiptItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class DraftViewModelImpl : ViewModel(), DraftViewModel, CategoriesViewModel, CurrenciesViewModel {
    override val categories: StateFlow<List<Category>> =
        MutableStateFlow(Category.values().toList())

    override val currencies: StateFlow<List<Currency>> =
        MutableStateFlow(Currency.getAvailableCurrencies().toList())

    private val _receipt = MutableStateFlow(Receipt(
        Category.NotAssigned,
        "SC. SOME SRL",
        Date(),
        12.9,
        Currency.getInstance("RON"),
        emptyList()
    ))

    override val receipt: StateFlow<Receipt> = _receipt

    override fun setCategory(category: Category) {
        _receipt.value = _receipt.value.copy(category = category)
    }

    override fun setMerchant(merchant: String) {}

    override fun setDate(date: Date) {}

    override fun setTotal(price: Double) {}

    override fun setCurrency(currency: Currency) {}

    override fun updateItem(item: ReceiptItem) {}

    override fun deleteItem(item: ReceiptItem) {}

    override fun addItem(item: ReceiptItem) {}
}