package com.lucianbc.receiptscan.presentation.draft

import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.scanner.show
import com.lucianbc.receiptscan.domain.usecase.ManageDraftUseCase
import java.util.*
import javax.inject.Inject

class DraftViewModel @Inject constructor(
    private val useCaseFactory: ManageDraftUseCase.Factory
) : ViewModel() {
    lateinit var merchant: LiveData<String?>
    lateinit var date: LiveData<Date?>
    lateinit var total: LiveData<Float?>
    lateinit var currency: LiveData<Currency?>
    val category: LiveData<String> = MutableLiveData("Grocery")
    lateinit var products: LiveData<List<Product>?>
    lateinit var useCase: ManageDraftUseCase

    fun init(draftId: Long) {
        useCase = useCaseFactory.fetch(draftId)
        merchant = useCase.extract { it.receipt.merchantName }
        date = useCase.extract { it.receipt.date }
        total = useCase.extract { it.receipt.total }
        currency = useCase.extract { it.receipt.currency }
        products = useCase.extract { it.products }
    }

    private fun <T> ManageDraftUseCase.extract(
        extractor: ((DraftWithProducts) -> T)
    ): LiveData<T> =
        this.value.map(extractor).toLiveData()
}

@BindingAdapter("android:text")
fun setText(view: TextView, date: Date?) {
    view.text = date.show()
}

@BindingAdapter("android:text")
fun setText(view: EditText, float: Float?) {
    view.text = Editable.Factory().newEditable(float.toString())
}

@BindingAdapter("android:text")
fun setText(view: TextView, currency: Currency?) {
    view.text = currency?.currencyCode ?: ""
}