package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.Command
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.OcrWithImageProducer
import com.lucianbc.receiptscan.domain.service.TaggingService
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScanUseCase @Inject constructor(
    private val taggingService: TaggingService,
    private val draftsRepository: DraftsRepository
) {

    private val _state = BehaviorSubject.createDefault<State>(State.Idle)

    val state: Flowable<State>
        get() = _state
            .toFlowable(BackpressureStrategy.LATEST)

    fun scan(frame: OcrWithImageProducer): ConnectableObservable<Long> =
        frame.produce()
            .doOnSubscribe { _state.onNext(State.OCR) }
            .observeOn(Schedulers.computation())
            .doOnNext { _state.onNext(State.Tagging) }
            .doOnNext { _state.onNext(State.Saving) }
            .flatMap { draftsRepository.create(Command(it)) }
            .doOnComplete { _state.onNext(State.Idle) }
            .doOnError { _state.onNext(State.Error) }
            .publish()



    sealed class State {
        object OCR: State()
        object Tagging: State()
        object Saving: State()
        object Idle: State()
        object Error: State()
    }
}
