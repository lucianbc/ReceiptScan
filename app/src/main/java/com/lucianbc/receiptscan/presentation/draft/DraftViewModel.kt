package com.lucianbc.receiptscan.presentation.draft

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.drafts.Draft
import com.lucianbc.receiptscan.domain.drafts.DraftsUseCase
import com.lucianbc.receiptscan.domain.drafts.Product
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.util.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DraftViewModel @Inject constructor(
    private val draftsUseCase: DraftsUseCase
) : ViewModel() {
    val merchant = mld<String>()
    val date = mld<Date>()
    val total = mld<String>()
    val currency = mld<String>()
    val category = mld<Category>()
    val products = mld<List<Product>>()

    val receiptImage = mld<Bitmap>()

    private lateinit var useCase: DraftsUseCase.Manage
    private val disposables = CompositeDisposable()

    fun init(draftId: Long) {
        useCase = draftsUseCase.fetch(draftId).apply {
            extract { it.merchantName ?: "" }.also(merchant::sourceFirst)
            extract { it.date }.also(date::sourceFirst)
            extract { it.total.show() }.also(total::sourceFirst)
            extract { it.currency.show() }.also(currency::sourceFirst)
            extract { it.products }.also(products::sourceFirst)
            extract { it.category }.also(category::sourceFirst)
            image.toLiveData().also { receiptImage.source(it) }
        }
    }

    val updateMerchant =
        debounced<String>(disposables, TIMEOUT, TIME_UNIT) {
            useCase.update(it) { v, dwp -> dwp.copy(merchantName = v) }
                .subscribe()
        }

    val updateTotal =
        debounced<Float>(disposables, TIMEOUT, TIME_UNIT) {
            useCase.update(it) { v, dwp -> dwp.copy(total = v) }
                .subscribe()
        }

    val updateDate =
        debounced<Date>(disposables, TIMEOUT, TIME_UNIT) {
            useCase.update(it) { v, dwp -> dwp.copy(date = v) }
                .subscribe()
        }

    val updateProduct =
        debounced<Product>(disposables, TIMEOUT, TIME_UNIT) {
            useCase.updateProduct(it)
                .subscribe()
        }

    val updateCurrency =
        debounced<Currency>(disposables, TIMEOUT, TIME_UNIT) { c ->
            useCase
                .update(c) { v, dwp -> dwp.copy(currency = v) }
                .andThen {
                    currency.postValue(c.show())
                }
                .subscribe()
        }


    val updateCategory =
        debounced<Category>(disposables, TIMEOUT, TIME_UNIT) { c ->
            useCase
                .update(c) { v, dwp -> dwp.copy(category = v) }
                .andThen { category.postValue(c) }
                .subscribe()
        }

    fun discardDraft() {
        useCase
            .delete()
            .subscribe()
            .addTo(disposables)
    }

    fun validateDraft() {
        useCase
            .moveToValid()
            .subscribe()
            .addTo(disposables)
    }

    fun createProduct() {
        useCase
            .createProduct()
            .subscribe({ newProd ->
                products.value?.let {
                    products.postValue(it + newProd)
                }
            }, {
                loge("Product failed to be inserted", it)
            })
            .addTo(disposables)
    }


    fun deleteProduct(prod: Product) {
        useCase
            .deleteProduct(prod)
            .subscribe {
                products.value?.let {
                    products.postValue(it.filter { x -> x != prod })
                }
            }
            .addTo(disposables)
    }

    private fun <T> DraftsUseCase.Manage.extract(
        extractor: (Draft) -> T
    ) = this.value.map(extractor).toLiveData()

    private fun <T> MediatorLiveData<T>.source(source: LiveData<T>) {
        this.addSource(source) { value = it }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    companion object {
        private val TIME_UNIT = TimeUnit.MILLISECONDS
        private const val TIMEOUT = 500L
    }
}

