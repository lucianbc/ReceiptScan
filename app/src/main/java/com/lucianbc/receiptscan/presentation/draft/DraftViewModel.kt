package com.lucianbc.receiptscan.presentation.draft

import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.scanner.show
import java.util.*
import javax.inject.Inject

class DraftViewModel @Inject constructor() : ViewModel() {
    val merchant: LiveData<String> = MutableLiveData("S. C. PROFI ROM FOOD S.R.L.")
    val date: LiveData<Date> = MutableLiveData(Date())
    val total: LiveData<Float> = MutableLiveData(11.69F)
    val currency: LiveData<Currency> = MutableLiveData(Currency.getInstance("RON"))
    val category: LiveData<String> = MutableLiveData("Grocery")
    val products: LiveData<List<Product>> = MutableLiveData(listOf(
        Product("PRODUS 1", 11.9f),
        Product("Produs 2", 94.0f),
        Product("PRODUS 1", 11.9f),
        Product("Produs 2", 94.0f),
        Product("PRODUS 1", 11.9f),
        Product("Produs 2", 94.0f),
        Product("PRODUS 1", 11.9f),
        Product("Produs 2", 94.0f),
        Product("PRODUS 1", 11.9f),
        Product("Produs 2", 94.0f),
        Product("PRODUS 1", 11.9f),
        Product("Produs 2", 94.0f)
    ))
}

@BindingAdapter("android:text")
fun setText(view: TextView, date: Date) {
    view.text = date.show()
}

@BindingAdapter("android:text")
fun setText(view: EditText, float: Float) {
    view.text = Editable.Factory().newEditable(float.toString())
}

@BindingAdapter("android:text")
fun setText(view: TextView, currency: Currency) {
    view.text = currency.currencyCode
}