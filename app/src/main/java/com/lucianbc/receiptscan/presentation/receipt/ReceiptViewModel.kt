package com.lucianbc.receiptscan.presentation.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.usecase.ManageReceiptUseCase
import com.lucianbc.receiptscan.util.mld
import com.lucianbc.receiptscan.util.show
import com.lucianbc.receiptscan.util.sourceFirst
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.*
import javax.inject.Inject

class ReceiptViewModel @Inject constructor(
    private val useCaseFactory: ManageReceiptUseCase.Factory
) : ViewModel() {
    private lateinit var useCase: ManageReceiptUseCase

    private val disposables = CompositeDisposable()

    val merchant = mld<String>()
    val date = mld<Date>()
    val total = mld<String>()
    val currency = mld<String>()
    val category = mld<Category>()
    val products = mld<List<Product>>()

    fun init(receiptId: Long) {
        useCase = useCaseFactory.fetch(receiptId).apply {
            extract { it.merchantName }.also(merchant::sourceFirst)
            extract { it.date }.also(date::sourceFirst)
            extract { it.total.show() }.also(total::sourceFirst)
            extract { it.currency.show() }.also(currency::sourceFirst)
            extract { it.category }.also(category::sourceFirst)
            extract { it.products }.also(products::sourceFirst)
        }
    }

    fun exportText(exporter: (String) -> Unit) {
        useCase
            .exportReceipt()
            .subscribe(exporter).addTo(disposables)
    }

    fun exportImage(exporter: (String) -> Unit) {
        useCase
            .exportPath()
            .subscribe(exporter).addTo(disposables)
    }

    override fun onCleared() {
        disposables.clear()
    }

    private fun <T> ManageReceiptUseCase.extract(
        extractor: (ManageReceiptUseCase.Value) -> T
    ) = this.receipt.map(extractor).toLiveData()
}