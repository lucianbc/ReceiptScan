package com.lucianbc.receiptscan.presentation.home.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.model.Category
import java.util.*
import javax.inject.Inject

class SettingsViewModel @Inject constructor(): ViewModel() {
    val currency = MutableLiveData(Currency.getInstance("RON"))
    val category = MutableLiveData(Category.Grocery)
    val sendEnabled = MutableLiveData(true)

    fun toggleSend() {
        sendEnabled.value = sendEnabled.value?.not()
    }

    fun update(value: Currency) {
        currency.value = value
    }

    fun update(value: Category) {
        category.value = value
    }
}