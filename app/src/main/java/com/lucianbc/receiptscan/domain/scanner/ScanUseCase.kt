package com.lucianbc.receiptscan.domain.scanner

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.viewfinder.OcrElements
import com.lucianbc.receiptscan.domain.viewfinder.Scannable
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScanUseCase @Inject constructor(
    private val draftsRepository: DraftsRepository
) {

    private val _state = BehaviorSubject.createDefault<State>(State.Idle)

    val state: Flowable<State>
        get() = _state
            .toFlowable(BackpressureStrategy.LATEST)

    fun scan(frame: Scannable): ConnectableObservable<Long> =
        frame.ocrElements()
            .zipWith(frame.image(), BiFunction { t1: OcrElements, t2: Bitmap -> t2 to t1 })
            .doOnSubscribe { _state.onNext(State.OCR) }
            .observeOn(Schedulers.computation())
            .map { DraftValue.fromOcrElementsAndImage(it.first, it.second) }
            .doOnNext { _state.onNext(State.Saving) }
            .flatMap { draftsRepository.create(it) }
            .doOnComplete { _state.onNext(State.Idle) }
            .doOnError { _state.onNext(State.Error) }
            .publish()

    sealed class State {
        object OCR : State()
        object Saving : State()
        object Idle : State()
        object Error : State()
    }
}
