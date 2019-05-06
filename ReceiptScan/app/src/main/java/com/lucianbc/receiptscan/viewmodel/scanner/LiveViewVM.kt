package com.lucianbc.receiptscan.viewmodel.scanner

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.viewmodel.Event
import org.greenrobot.eventbus.EventBus

class LiveViewVM: ViewModel() {
    @Bindable
    val flash = MutableLiveData<Boolean>(false)

    fun toggleFlash() {
        Log.d("LiveViewVM", "Toggle flash")
        flash.value = flash.value?.not()
    }

    fun importImage() {
        EventBus.getDefault().post(Event.ImportImage)
    }
}