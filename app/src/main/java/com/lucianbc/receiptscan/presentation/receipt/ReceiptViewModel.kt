package com.lucianbc.receiptscan.presentation.receipt

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.ProductEntity
import com.lucianbc.receiptscan.domain.usecase.ManageReceiptUseCase
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import com.lucianbc.receiptscan.util.mld
import com.lucianbc.receiptscan.util.show
import com.lucianbc.receiptscan.util.sourceFirst
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.*
import javax.inject.Inject

class ReceiptViewModel @Inject constructor(
    private val useCaseFactory: ManageReceiptUseCase.Factory,
    private val imagesDao: ImagesDao
) : ViewModel() {
    private lateinit var useCase: ManageReceiptUseCase

    private val disposables = CompositeDisposable()

    val merchant = mld<String>()
    val date = mld<Date>()
    val total = mld<String>()
    val currency = mld<String>()
    val category = mld<Category>()
    val products = mld<List<ProductEntity>>()

    fun init(receiptId: Long) {
        useCase = useCaseFactory.fetch(receiptId).apply {
            extract { it.merchantName }.also(merchant::sourceFirst)
            extract { it.date }.also(date::sourceFirst)
            extract { it.total.show() }.also(total::sourceFirst)
            extract { it.currency.show() }.also(currency::sourceFirst)
            extract { it.category }.also(category::sourceFirst)
            extract { it.productEntities }.also(products::sourceFirst)
        }
    }

    fun exportText(exporter: (String) -> Unit) {
        useCase
            .exportReceipt()
            .subscribe(exporter).addTo(disposables)
    }

    fun exportImage(exporter: (Uri) -> Unit, onError: (Throwable) -> Unit) {
        useCase
            .exportPath()
            .map(imagesDao::accessFile)
            .subscribe(exporter, onError)
            .addTo(disposables)
    }

    fun exportBoth(exporter: (String, Uri) -> Unit, onError: (Throwable) -> Unit) {
        useCase
            .exportBoth()
            .map { it.first to imagesDao.accessFile(it.second) }
            .subscribe({ exporter(it.first, it.second) }, onError)
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.clear()
    }

    private fun <T> ManageReceiptUseCase.extract(
        extractor: (ManageReceiptUseCase.Value) -> T
    ) = this.receipt.map(extractor).toLiveData()
}