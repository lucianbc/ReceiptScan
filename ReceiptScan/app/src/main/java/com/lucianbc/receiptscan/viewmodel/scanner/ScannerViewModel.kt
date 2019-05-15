package com.lucianbc.receiptscan.viewmodel.scanner

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.repository.draft
import com.lucianbc.receiptscan.domain.service.ReceiptScanner
import com.lucianbc.receiptscan.viewmodel.Event
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class ScannerViewModel @Inject constructor (
    private val eventBus: EventBus,
    private val receiptScanner: ReceiptScanner
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
                eventBus.post(Event.ImageScanned(it.draft.id))
            }
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
