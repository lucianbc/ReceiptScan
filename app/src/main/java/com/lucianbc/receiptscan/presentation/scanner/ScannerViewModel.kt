package com.lucianbc.receiptscan.presentation.scanner

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.scanner.ScanUseCase
import com.lucianbc.receiptscan.domain.extract.Scannable
import com.lucianbc.receiptscan.infrastructure.ScannableFactory
import com.lucianbc.receiptscan.presentation.Event
import com.lucianbc.receiptscan.util.loge
import com.lucianbc.receiptscan.util.map
import com.otaliastudios.cameraview.PictureResult
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class ScannerViewModel @Inject constructor(
    private val scanUseCase: ScanUseCase,
    private val factory: ScannableFactory,
    private val eventBus: EventBus
) : ViewModel() {
    sealed class State {
        object NoPermission : State()
        object Allowed : State()
        object Error : State()
        object Processing : State()
    }

    private val disposables = CompositeDisposable()

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    val isImportAllowed: LiveData<Boolean>
        get() = _state.map { it != State.Processing }

    fun noPermission() {
        _state.value = State.NoPermission
    }

    fun allowed() {
        _state.value = State.Allowed
    }

    private fun processing() {
        _state.value = State.Processing
    }

    fun process(pictureResult: PictureResult) {
        processing()
        factory.create(pictureResult).scan()
    }

    fun process(image: Observable<Bitmap>) {
        processing()
        factory.create(image).scan()
    }

    private fun Scannable.scan() {
        scanUseCase
            .scan(this)
            .subscribe(
                { eventBus.post(Event.ImageScanned(it)) },
                { loge("Error when processing camera picture", it) }
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}