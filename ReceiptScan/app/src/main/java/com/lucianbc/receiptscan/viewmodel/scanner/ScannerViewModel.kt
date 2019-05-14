package com.lucianbc.receiptscan.viewmodel.scanner

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.service.ReceiptScanner
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.viewmodel.Event
import com.lucianbc.receiptscan.viewmodel.ReceiptDraftCache
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class ScannerViewModel @Inject constructor (
    private val eventBus: EventBus,
    private val receiptScanner: ReceiptScanner,
    private val receiptDraftCache: ReceiptDraftCache
) : ViewModel() {

    sealed class State {
        object NoPermission: State()
        object Allowed: State()
        object Error: State()
        object Processing : State()
    }

    private val disposables = CompositeDisposable()

    val state = MutableLiveData<State>()

    fun importImage() = eventBus.post(Event.ImportImage)

    fun scanImage(imageProvider: Observable<Bitmap>) {
        state.value = State.Processing
        receiptScanner
            .scan(imageProvider)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                logd("Mothafuka finished")
                receiptDraftCache.provide(it)
                eventBus.post(Event.ImageScanned)
            }
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
