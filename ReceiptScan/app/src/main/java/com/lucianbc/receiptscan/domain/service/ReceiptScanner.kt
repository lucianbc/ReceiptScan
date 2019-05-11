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
import com.lucianbc.receiptscan.util.logd
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.Thread.currentThread
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReceiptScanner @Inject constructor(
    private val recognizer: FirebaseVisionTextRecognizer,
    private val imageRepository: ImageRepository,
    private val draftRepository: ReceiptDraftRepository
) {
    private val frameProducer = PublishSubject.create<FirebaseVisionImage>()

    val scanAnnotations: Flowable<ScanAnnotations> =
        frameProducer
            .throttleLast(FRAME_RATE, FRAME_UNIT)
            .flatMap(this::process)
            .map { it.toScanInfo() }
            .doOnNext { logd("Finished processing on thread ${currentThread().name}") }
            .toFlowable(BackpressureStrategy.LATEST)


    fun scan(bitmapProvider: Observable<Bitmap>): Observable<ReceiptDraft> =
        bitmapProvider
            .doOnNext {
                logd("OCR on thread ${currentThread().name}")
            }
            .flatMap { process(it) }
            .observeOn(Schedulers.io())
            .doOnNext { logd("Save on thread ${currentThread().name}") }
            .map { saveDraft(it) }
            .doOnNext {
                logd("Ajuns la final pe thread ${currentThread().name}")
                logd(it.toString())
            }

    fun processFrame(frame: FirebaseVisionImage) = frameProducer.onNext(frame)

    private fun process(image: Bitmap): Observable<Pair<Bitmap, FirebaseVisionText>> {
        val result = PublishSubject
            .create<Pair<Bitmap, FirebaseVisionText>>()
        recognizer
            .processImage(image.toFirebaseImage())
            .addOnSuccessListener { result.onNext(image to it) }
            .addOnFailureListener { result.onError(it) }
        return result
    }

    private fun process(image: FirebaseVisionImage): Observable<FirebaseVisionText> {
        val result = PublishSubject
            .create<FirebaseVisionText>()

        recognizer
            .processImage(image)
            .addOnSuccessListener { result.onNext(it) }
            .addOnFailureListener { result.onError(it) }
        return result
    }

    private fun saveDraft(rawData: Pair<Bitmap, FirebaseVisionText>): ReceiptDraft {
        val filePath = imageRepository.saveImage(rawData.first)
        val annotations = rawData.second.toScanInfo()
        return draftRepository.saveDraft(filePath, annotations)
    }

    private fun Bitmap.toFirebaseImage(): FirebaseVisionImage {
        return FirebaseVisionImage.fromBitmap(this)
    }

    private fun FirebaseVisionText.toScanInfo(): ScanAnnotations {
        return this.textBlocks
            .flatMap { it.lines }
            .map { ScanInfoBox(
                it.boundingBox!!.top,
                it.boundingBox!!.bottom,
                it.boundingBox!!.left,
                it.boundingBox!!.right,
                it.text
            ) }
    }

    companion object {
        private const val FPS = 1
        private val FRAME_UNIT = TimeUnit.MILLISECONDS
        private const val FRAME_RATE = ((1F / FPS) * 1000).toLong()
    }
}