package com.lucianbc.receiptscan.presentation.draft

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.scanner.show
import com.lucianbc.receiptscan.domain.usecase.ManageDraftUseCase
import com.lucianbc.receiptscan.presentation.icon
import com.lucianbc.receiptscan.util.debounced
import com.lucianbc.receiptscan.util.loge
import com.lucianbc.receiptscan.util.mld
import com.lucianbc.receiptscan.util.sourceFirst
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DraftViewModel @Inject constructor(
    private val useCaseFactory: ManageDraftUseCase.Factory
) : ViewModel() {
    val merchant = mld<String>()
    val date = mld<Date>()
    val total = mld<String>()
    val currency = mld<String>()
    val category = mld<Category>()
    val products = mld<List<Product>>()

    val image = mld<Bitmap>()


    private lateinit var useCase: ManageDraftUseCase
    private val disposables = CompositeDisposable()

    fun init(draftId: Long) {
        useCase = useCaseFactory.fetch(draftId)
        merchant.sourceFirst(useCase.extract { it.receipt.merchantName ?: "" })
        date.sourceFirst(useCase.extract { it.receipt.date ?: Date() })
        total.sourceFirst(useCase.extract { (it.receipt.total?: 0f).toString() })
        currency.sourceFirst(useCase.extract { it.receipt.currency.show() })
        products.sourceFirst(useCase.extract { it.products })
        category.sourceFirst(useCase.extract { it.receipt.category })
        image.source(useCase.image.toLiveData())
    }

    val updateMerchant =
        debounced<String>(disposables, TIMEOUT, TIME_UNIT) {
            useCase.update(it) { v, dwp -> dwp.receipt.copy(merchantName = v) }
        }

    val updateTotal =
        debounced<Float>(disposables, TIMEOUT, TIME_UNIT) {
            useCase.update(it) { v, dwp -> dwp.receipt.copy(total = v) }
        }

    val updateDate =
        debounced<Date>(disposables, TIMEOUT, TIME_UNIT) {
            useCase.update(it) { v, dwp -> dwp.receipt.copy(date = v) }
        }

    val updateProduct =
        debounced<Product>(disposables, TIMEOUT, TIME_UNIT) {
            useCase.updateProduct(it)
        }

    val updateCurrency =
        debounced<Currency>(disposables, TIMEOUT, TIME_UNIT) { c ->
            useCase
                .updateSubs(c) { v, dwp -> dwp.receipt.copy(currency = v) }
                .andThen { currency.postValue(c.show()) }
                .subscribe()
        }


    val updateCategory =
        debounced<Category>(disposables, TIMEOUT, TIME_UNIT) { c ->
            useCase
                .updateSubs(c) { v, dwp -> dwp.receipt.copy(category = v) }
                .andThen { category.postValue(c) }
                .subscribe()

        }


    fun discardDraft() {
        useCase
            .deleteDraft()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .addTo(disposables)
    }

    fun validateDraft() {
        useCase
            .validateDraft()
            .subscribeOn(Schedulers.io())
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

    private fun <T> ManageDraftUseCase.extract(
        extractor: ((DraftWithProducts) -> T)
    ): LiveData<T> =
        this.value.map(extractor).toLiveData()



    private fun Currency?.show() = this?.currencyCode ?: ""

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

@BindingAdapter("android:text")
fun setText(view: TextView, date: Date?) {
    view.text = date.show()
}

@BindingAdapter("android:src")
fun setIcon(view: ImageView, value: Category?) {
    value?.let { view.setImageResource(value.icon) }
}