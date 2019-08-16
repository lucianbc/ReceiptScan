package com.lucianbc.receiptscan.presentation.home.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.infrastructure.dao.PreferencesDao
import java.util.*
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val preferencesDao: PreferencesDao
): ViewModel() {
    private val _currency = MutableLiveData(preferencesDao.currency)
    private val _category = MutableLiveData(preferencesDao.category)
    private val _sendEnabled = MutableLiveData(preferencesDao.enabled)

    val currency : LiveData<Currency>
        get() = _currency
    val category : LiveData<Category>
        get() = _category

    val sendEnabled : LiveData<Boolean>
        get() = _sendEnabled

    fun toggleSend() {
        val newValue = sendEnabled.value?.not() ?: true
        preferencesDao.setSendReceipt(newValue)
        _sendEnabled.value = newValue
    }

    fun update(value: Currency) {
        preferencesDao.setCurrency(value)
        _currency.value = value
    }

    fun update(value: Category) {
        preferencesDao.setCategory(value)
        _category.value = value
    }
}