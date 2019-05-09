package com.lucianbc.receiptscan.domain.service

import android.graphics.Bitmap
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanAnnotations
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.lucianbc.receiptscan.domain.repository.ImageRepository
import com.lucianbc.receiptscan.domain.repository.ReceiptDraftRepository
import com.lucianbc.receiptscan.util.logd
import com.otaliastudios.cameraview.Frame
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.Exception
import java.lang.Thread.currentThread
import javax.inject.Inject

class ReceiptScanner @Inject constructor(
    private val recognizer: FirebaseVisionTextRecognizer,
    private val imageRepository: ImageRepository,
    private val draftRepository: ReceiptDraftRepository
) {
    private val scanInfoOutput = PublishSubject.create<ScanAnnotations>()

    fun processFrame(frame: Frame) {
        val subject = scanInfoOutput
        recognizer
            .processImage(frame.toFirebaseImage())
            .addOnSuccessListener {
                subject.onNext(it.toScanInfo())
            }
            .addOnFailureListener(subject::onError)
    }

    fun scan(bitmapProvider: () -> Bitmap): Observable<ReceiptDraft> = Observable
        .fromCallable {
            logd("Read file on thread ${currentThread().name}")
            bitmapProvider()
        }
        .flatMap {
            logd("OCR on thread ${currentThread().name}")
            process(it)
        }
        .observeOn(Schedulers.io())
        .map {
            logd("Save on thread ${currentThread().name}")
            saveDraft(it)
        }
        .subscribeOn(Schedulers.io())
        .map {
            logd("Ajuns la final pe thread ${currentThread().name}")
            logd(it.toString())
            it
        }

    private fun process(image: Bitmap): PublishSubject<Pair<Bitmap, FirebaseVisionText>> {
        val result = PublishSubject
            .create<Pair<Bitmap, FirebaseVisionText>>()
        recognizer
            .processImage(image.toFirebaseImage())
            .addOnSuccessListener { result.onNext(image to it) }
            .addOnFailureListener { result.onError(it) }
        return result
    }

    private fun saveDraft(rawData: Pair<Bitmap, FirebaseVisionText>): ReceiptDraft {
        val filePath = imageRepository.saveImage(rawData.first)
        val annotations = rawData.second.toScanInfo()
        return draftRepository.saveDraft(filePath, annotations)
    }

    fun scanInfoSubscribe(subscriber: Observer<ScanAnnotations>) {
        scanInfoOutput.subscribe(subscriber)
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

    private fun rotation(rotation: Int): Int {
        return when (rotation) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> FirebaseVisionImageMetadata.ROTATION_0
        }
    }

    private fun Frame.toFirebaseImage(): FirebaseVisionImage {
        val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(this.format)
            .setRotation(rotation(this.rotation))
            .setHeight(this.size.height)
            .setWidth(this.size.width)
            .build()
        return FirebaseVisionImage.fromByteArray(this.data, metadata)
    }
}