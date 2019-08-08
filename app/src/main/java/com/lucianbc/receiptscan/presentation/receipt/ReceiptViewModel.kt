package com.lucianbc.receiptscan.presentation.receipt

import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.util.mld
import com.lucianbc.receiptscan.util.show
import java.util.*
import javax.inject.Inject

class ReceiptViewModel @Inject constructor() : ViewModel() {

    val merchant = mld("Merchant")
    val date = mld(Date())
    val total = mld("9.99")
    val currency = mld(Currency.getInstance("RON").show())
    val category = mld(Category.Grocery)
    val products = mld(emptyList<Product>())
}