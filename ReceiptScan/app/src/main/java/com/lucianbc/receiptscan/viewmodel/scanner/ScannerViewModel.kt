package com.lucianbc.receiptscan.viewmodel.scanner

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.lucianbc.receiptscan.domain.service.ReceiptScanner
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.viewmodel.Event
import com.otaliastudios.cameraview.Frame
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class ScannerViewModel @Inject constructor(
    private val eventBus: EventBus,
    private val receiptScanner: ReceiptScanner
) : ViewModel() {
    sealed class State {
        object NoPermission: State()
        object Allowed: State()
        object Error: State()
    }

    val state = MutableLiveData<State>()

    fun importImage() {
        logd("Import image in vm")
        eventBus.post(Event.ImportImage)
    }

    fun scanImage(imageProvider: Observable<Bitmap>) {
        receiptScanner.scan(imageProvider).subscribe()
    }
}