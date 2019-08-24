package com.lucianbc.receiptscan.domain.extract

import com.lucianbc.receiptscan.domain.viewfinder.OcrElements
import com.lucianbc.receiptscan.domain.viewfinder.Scannable
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExtractUseCaseImpl @Inject constructor(
    fps: Float,
    private val repository: ExtractRepository,
    private val extractor: Extractor
) : ExtractUseCase {

    override val state: Flowable<State> =
        stateProcessing

    override val preview: Flowable<OcrElements> =
        frameSourceProcessing

    override fun fetchPreview(frame: Scannable) =
        _frameSource.onNext(frame)

    override fun extract(frame: Scannable): Observable<DraftId> {
        return if (_state.value != State.Idle)
            Observable.error(IllegalArgumentException("Use case is not in Idle mode"))
        else frame.ocrElements()
            .zipWith(frame.image())
            .observeOn(Schedulers.computation())
            .map { (els, img) -> extractor(els) to img }
            .flatMapSingle { (draft, img) -> repository.persist(draft, img) }
            .doOnSubscribe { _state.onNext(State.Processing) }
            .doOnComplete { _state.onNext(State.Idle) }
            .doOnError { _state.onNext(State.Error(it)) }
    }

    private val stateProcessing
        get() = _state.toFlowable(BackpressureStrategy.LATEST)

    private val _state = BehaviorSubject.createDefault<State>(State.Idle)

    private val frameSourceProcessing
        get() = _frameSource
            .toFlowable(BackpressureStrategy.LATEST)
            .observeOn(Schedulers.computation())
            .flatMap {
                it
                    .ocrElements()
                    .toFlowable(BackpressureStrategy.LATEST)
                    .onErrorResumeNext(Flowable.empty())
            }
            .onBackpressureLatest()
            .throttleLast(frameRate, FRAME_UNIT)

    private val _frameSource = BehaviorSubject.create<Scannable>()

    private val frameRate = (1000 / fps).toLong()

    companion object {
        private val FRAME_UNIT = TimeUnit.MILLISECONDS
    }
}