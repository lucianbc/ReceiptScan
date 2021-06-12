package com.lucianbc.receiptscan.v2.ui.viewModels

import com.lucianbc.receiptscan.v2.domain.Category
import com.lucianbc.receiptscan.v2.ui.screens.Receipt
import com.lucianbc.receiptscan.v2.ui.screens.ReceiptItem
import kotlinx.coroutines.flow.StateFlow
import java.util.*

interface DraftViewModel {
    val receipt: StateFlow<Receipt>

    fun setCategory(category: Category)
    fun setMerchant(merchant: String)
    fun setDate(date: Date)
    fun setTotal(price: Double)
    fun setCurrency(currency: Currency)
    fun updateItem(item: ReceiptItem)
    fun deleteItem(item: ReceiptItem)
    fun addItem(item: ReceiptItem)

    object Empty : DraftViewModel by Empty
}