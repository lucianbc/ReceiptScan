package com.lucianbc.receiptscan.presentation.home.exports.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.export.Session
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

    fun validateInput() = Session.validate(
        firstDate.value,
        lastDate.value,
        content(),
        format()
    )

    private fun content(): Session.Content? =
        contentOption.value?.let {
            when(it) {
                R.id.contentText -> Session.Content.TextOnly
                else -> Session.Content.TextAndImage
            }
        }

    private fun format(): Session.Format? =
        Session.Format.JSON
//        formatOption.value?.let {
//            when(it) {
//                R.id.contentCsv -> Session.Format.CSV
//                else -> Session.Format.JSON
//            }
//        }
}