package com.lucianbc.receiptscan.domain.service

import android.graphics.Bitmap
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanAnnotations
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.lucianbc.receiptscan.domain.repository.ImageRepository
import com.lucianbc.receiptscan.domain.repository.ReceiptDraftRepository
import com.lucianbc.receiptscan.viewmodel.DraftWithImage
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptScanner @Inject constructor(
    private val recognizer: FirebaseVisionTextRecognizer,
    private val imageRepository: ImageRepository,
    private val draftRepository: ReceiptDraftRepository
) {
    private val frameProducer = PublishSubject.create<FirebaseVisionImage>()

    val scanAnnotations: Flowable<ScanAnnotations> =
        frameProducer
            .throttleLast(FRAME_RATE, FRAME_UNIT)
            .flatMap(this::extractText)
            .map { it.toScanInfo() }
            .toFlowable(BackpressureStrategy.LATEST)


    private val _state = BehaviorSubject.createDefault<State>(State.Idle)
    val state: Flowable<State>
        get() = _state
            .toFlowable(BackpressureStrategy.LATEST)


    fun scan(bitmapProvider: Observable<Bitmap>): Observable<DraftWithImage> =
        bitmapProvider
            .doOnNext { _state.onNext(State.OCR) }
            .map { i -> i to i.firebaseImage() }
            .flatMap { (i, f) -> extractText(f).map { t -> i to t } }
            .doOnNext { _state.onNext(State.Saving) }
            .map { saveDraft(it) }
            .doOnNext { _state.onNext(State.Idle) }
            .doOnSubscribe { _state.onNext(State.ReadingImage) }


    fun processFrame(frame: FirebaseVisionImage) = frameProducer.onNext(frame)

    private fun extractText(image: FirebaseVisionImage): Observable<FirebaseVisionText> {
        val result = PublishSubject
            .create<FirebaseVisionText>()

        recognizer
            .processImage(image)
            .addOnSuccessListener { result.onNext(it) }
            .addOnFailureListener { result.onError(it) }
        return result
    }

    private fun saveDraft(rawData: Pair<Bitmap, FirebaseVisionText>): DraftWithImage {
        val filePath = imageRepository.saveImage(rawData.first)
        val annotations = rawData.second.toScanInfo()
        val draft = draftRepository.saveDraft(filePath, annotations)
        return draft to rawData.first
    }

    private fun Bitmap.firebaseImage() = FirebaseVisionImage.fromBitmap(this)

    private fun FirebaseVisionText.toScanInfo(): ScanAnnotations =
        this.textBlocks
            .flatMap { it.lines }
            .map { ScanInfoBox(
                it.boundingBox!!.top,
                it.boundingBox!!.bottom,
                it.boundingBox!!.left,
                it.boundingBox!!.right,
                it.text
            ) }

    sealed class State {
        object ReadingImage: State()
        object OCR: State()
        object Saving: State()
        object Idle: State()
    }

    companion object {
        private const val FPS = 1
        private val FRAME_UNIT = TimeUnit.MILLISECONDS
        private const val FRAME_RATE = ((1F / FPS) * 1000).toLong()
    }
}