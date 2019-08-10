package com.lucianbc.receiptscan.presentation.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.usecase.ManageReceiptUseCase
import com.lucianbc.receiptscan.util.mld
import com.lucianbc.receiptscan.util.show
import com.lucianbc.receiptscan.util.sourceFirst
import java.util.*
import javax.inject.Inject

class ReceiptViewModel @Inject constructor(
    private val useCaseFactory: ManageReceiptUseCase.Factory
) : ViewModel() {
    val merchant = mld<String>()
    val date = mld<Date>()
    val total = mld<String>()
    val currency = mld<String>()
    val category = mld<Category>()
    val products = mld<List<Product>>()

    fun init(receiptId: Long) {
        val useCase = useCaseFactory.fetch(receiptId)

        merchant.sourceFirst(useCase.extract { it.merchantName })
        date.sourceFirst(useCase.extract { it.date })
        total.sourceFirst(useCase.extract { it.total.show() })
        currency.sourceFirst(useCase.extract { it.currency.show() })
        category.sourceFirst(useCase.extract { it.category })
        products.sourceFirst(useCase.extract { it.products })
    }

    private fun <T> ManageReceiptUseCase.extract(
        extractor: (ManageReceiptUseCase.Value) -> T
    ) = this.receipt.map(extractor).toLiveData()
}