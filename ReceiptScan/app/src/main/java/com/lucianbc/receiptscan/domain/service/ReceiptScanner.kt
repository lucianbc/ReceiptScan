package com.lucianbc.receiptscan.domain.service

import android.graphics.Bitmap
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanAnnotations
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.otaliastudios.cameraview.Frame
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject
import java.lang.Exception

class ReceiptScanner(
    private val recognizer: FirebaseVisionTextRecognizer,
    private val imageRepository: ImageRepository,
    private val draftRepository: ReceiptDraftRepository
) {
    private val receiptDraftOutput = PublishSubject.create<ReceiptDraft>()
    private val scanInfoOutput = PublishSubject.create<ScanAnnotations>()

    fun processFrame(frame: Frame) {
        val subject = scanInfoOutput
        val metadata = frame.toFirebaseMetadata()
        val image = FirebaseVisionImage.fromByteArray(frame.data, metadata)
        recognizer
            .processImage(image)
            .addOnSuccessListener {
                subject.onNext(it.toScanInfo())
            }
            .addOnFailureListener(subject::onError)
    }

    fun scan(image: Bitmap) {
        val subject = receiptDraftOutput
        recognizer
            .processImage(image.toFirebaseImage())
            .addOnSuccessListener {
                try {
                    val filePath = imageRepository.saveImage(image)
                    val metadata = it.toScanInfo()
                    val receiptDraft = draftRepository.saveDraft(filePath, metadata)
                    subject.onNext(receiptDraft)
                } catch (e: Exception) {
                    subject.onError(e)
                }
            }
            .addOnFailureListener (receiptDraftOutput::onError)
    }

    fun scanInfoSubscribe(subscriber: Observer<ScanAnnotations>) {
        scanInfoOutput.subscribe(subscriber)
    }

    fun receiptDraftSubscribe(subscriber: Observer<ReceiptDraft>) {
        receiptDraftOutput.subscribe(subscriber)
    }

    private fun Bitmap.toFirebaseImage(): FirebaseVisionImage {
        return FirebaseVisionImage.fromBitmap(this)
    }

    private fun FirebaseVisionText.toScanInfo(): Collection<ScanInfoBox> {
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

    private fun Frame.toFirebaseMetadata() =
        FirebaseVisionImageMetadata.Builder()
            .setFormat(this.format)
            .setRotation(rotation(this.rotation))
            .setHeight(this.size.height)
            .setWidth(this.size.width)
            .build()
}