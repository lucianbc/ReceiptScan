package com.lucianbc.receiptscan.viewmodel.scanner

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucianbc.receiptscan.domain.service.ReceiptScanner
import com.lucianbc.receiptscan.viewmodel.Event
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class LiveViewVM @Inject constructor(
    private val eventBus: EventBus,
    private val receiptScanner: ReceiptScanner
): ViewModel() {
    val flash = MutableLiveData<Boolean>(false)

    fun toggleFlash() {
        Log.d("LiveViewVM", "Toggle flash")
        flash.value = flash.value?.not()
    }

    fun importImage() {
        eventBus.post(Event.ImportImage)
    }
//
//    class Factory(private val eventBus: EventBus): ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return LiveViewVM(eventBus) as T
//        }
//    }
}