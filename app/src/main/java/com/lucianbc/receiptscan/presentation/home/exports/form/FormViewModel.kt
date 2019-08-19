package com.lucianbc.receiptscan.presentation.home.exports.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.util.map
import java.util.*
import javax.inject.Inject

class FormViewModel @Inject constructor() : ViewModel() {

    sealed class Option {
        object Next: Option()
        object Check: Option()
    }

    val contentOption = MutableLiveData<Int>()
    val formatOption = MutableLiveData<Int>()
    val firstDate = MutableLiveData(Date())
    val lastDate = MutableLiveData(Date())
    val option = MutableLiveData<Option>(Option.Next)
    val isCheck = option.map { it == Option.Check }


    fun handleOption() {
        logd(contentOption.value.toString())
        logd(formatOption.value.toString())
        logd(firstDate.value.toString())
        logd(lastDate.value.toString())
    }
}