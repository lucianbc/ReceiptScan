package com.lucianbc.receiptscan.viewmodel.scanner

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.viewmodel.Event
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class LiveViewVM @Inject constructor(
    private val eventBus: EventBus
): ViewModel() {
    val flash = MutableLiveData<Boolean>(false)

    fun toggleFlash() {
        Log.d("LiveViewVM", "Toggle flash")
        flash.value = flash.value?.not()
    }

    fun importImage() {
        eventBus.post(Event.ImportImage)
    }
}