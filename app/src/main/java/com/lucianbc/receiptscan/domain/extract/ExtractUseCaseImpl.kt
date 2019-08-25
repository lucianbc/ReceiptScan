package com.lucianbc.receiptscan.domain.extract

import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class ExtractUseCaseImpl @AssistedInject constructor(
    @Assisted fps: Float,
    private val repository: ExtractRepository,
    private val extractor: Extractor
) : ExtractUseCase {

    override val state: Flowable<State>

    override val preview: Flowable<OcrElements>

    override fun fetchPreview(frame: Scannable)
            = _frameSource.onNext(frame)

    override fun extract(frame: Scannable): Single<DraftId> {
        return if (_state.value != State.Idle)
            Single.error(IllegalArgumentException("Use case is not in Idle mode"))
        else frame.ocrElements()
            .zipWith(frame.image())
            .singleOrError()
            .observeOn(Schedulers.computation())
            .map { (els, img) -> extractor(els) to img }
            .flatMap { (draft, img) -> repository.persist(draft, img) }
            .doOnSubscribe { _state.onNext(State.Processing) }
            .doOnSuccess { _state.onNext(State.Idle) }
            .doOnError { _state.onNext(State.Error(it)) }
    }

    private fun stateProcessing(): Flowable<State> {
        return _state.toFlowable(BackpressureStrategy.LATEST)
    }

    private fun frameSourceProcessing(): Flowable<OcrElements> {
        return _frameSource
            .toFlowable(BackpressureStrategy.LATEST)
            .observeOn(Schedulers.computation(), false, 1)
            .flatMap {
                it
                    .ocrElements()
                    .toFlowable(BackpressureStrategy.LATEST)
                    .onErrorResumeNext(Flowable.empty())
            }
            .onBackpressureLatest()
            .throttleLast(frameRate, FRAME_UNIT)
    }

    private val _state = BehaviorSubject.createDefault<State>(State.Idle)

    private val _frameSource = BehaviorSubject.create<Scannable>()

    private val frameRate: Long = (1000 / fps).toLong()

    init {
        state = stateProcessing()
        preview = frameSourceProcessing()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(fps: Float): ExtractUseCaseImpl
    }

    companion object {
        private val FRAME_UNIT = TimeUnit.MILLISECONDS
    }
}