package com.lucianbc.receiptscan.presentation.draft

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.scanner.show
import com.lucianbc.receiptscan.domain.usecase.ManageDraftUseCase
import com.lucianbc.receiptscan.util.debounced
import com.lucianbc.receiptscan.util.loge
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DraftViewModel @Inject constructor(
    private val useCaseFactory: ManageDraftUseCase.Factory
) : ViewModel() {
    val merchant = mld<String?>()
    val date = mld<Date?>()
    val total = mld<String?>()
    val currency = mld<Currency?>()
    val category = mld<String?>("Grocery")
    val products = mld<List<Product>>()


    private lateinit var useCase: ManageDraftUseCase
    private val disposables = CompositeDisposable()

    fun init(draftId: Long) {
        useCase = useCaseFactory.fetch(draftId)
        merchant.sourceFirst(useCase.extract { it.receipt.merchantName })
        date.sourceFirst(useCase.extract { it.receipt.date })
        total.sourceFirst(useCase.extract { it.receipt.total?.toString() })
        currency.sourceFirst(useCase.extract { it.receipt.currency })
        products.sourceFirst(useCase.extract { it.products })
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

    private fun <T> ManageDraftUseCase.extract(
        extractor: ((DraftWithProducts) -> T)
    ): LiveData<T> =
        this.value.map(extractor).toLiveData()

    private fun<T> mld(default: T) = MediatorLiveData<T>().apply { value = default }
    private fun<T> mld() = MediatorLiveData<T>()

    private fun <T> MediatorLiveData<T>.sourceFirst(source: LiveData<T>) {
        this.addSource(source) { t ->
            this.value = t
            this.removeSource(source)
        }
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

@BindingAdapter("android:text")
fun setText(view: TextView, currency: Currency?) {
    view.text = currency?.currencyCode ?: ""
}
