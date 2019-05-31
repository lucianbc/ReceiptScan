package com.lucianbc.receiptscan.infrastructure

import android.graphics.Bitmap
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.domain.model.OcrElement
import com.lucianbc.receiptscan.domain.model.OcrElements
import com.lucianbc.receiptscan.domain.service.OcrElementsProducer
import com.lucianbc.receiptscan.domain.service.OcrWithImageProducer
import com.otaliastudios.cameraview.Frame
import com.otaliastudios.cameraview.PictureResult
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class OcrElementsProducersFactory @Inject constructor(
    private val recognizer: FirebaseVisionTextRecognizer
) {
    fun simple(frame: Frame): OcrElementsProducer {
        return object : OcrElementsProducer {
            override fun produce(): Observable<OcrElements> {
                return Observable.just(frame)
                    .flatMap { it.firebaseImage().recognize() }
            }
        }
    }

    fun withImage(image: PictureResult): OcrWithImageProducer {
        return object : OcrWithImageProducer {
            override fun produce(): Observable<Pair<Bitmap, OcrElements>> =
                Observable.just(image)
                    .map { it.firebaseImage() }
                    .map { it.bitmap to it }
                    .flatMap { it.ocrElements() }
        }
    }

    fun withImage(imageSrc: Observable<Bitmap>): OcrWithImageProducer {
        return object : OcrWithImageProducer {
            override fun produce(): Observable<Pair<Bitmap, OcrElements>> =
                imageSrc
                    .map { it to it.firebaseImage() }
                    .flatMap { it.ocrElements() }
        }
    }

    private fun Pair<Bitmap, FirebaseVisionImage>.ocrElements(): Observable<Pair<Bitmap, OcrElements>> =
        this.second.recognize().map { this.first to it }

    private fun extractText(image: FirebaseVisionImage): Observable<FirebaseVisionText> {
        val result = PublishSubject
            .create<FirebaseVisionText>()

        recognizer
            .processImage(image)
            .addOnSuccessListener { result.onNext(it); result.onComplete() }
            .addOnFailureListener { result.onError(it) }

        return result
    }

    private fun FirebaseVisionImage.recognize(): Observable<OcrElements> =
        extractText(this)
            .map { it.textBlocks.flatMap { tb -> tb.lines } }
            .map { it.asSequence() }
            .map { it.mapNotNull { line -> line.toOcrElement() } }


    private fun FirebaseVisionText.Line.toOcrElement(): OcrElement? =
        if (this.boundingBox != null)
            OcrElement(this.text,
                this.boundingBox!!.top,
                this.boundingBox!!.left,
                this.boundingBox!!.bottom,
                this.boundingBox!!.right)
        else null

    private fun Bitmap.firebaseImage() = FirebaseVisionImage.fromBitmap(this)

    private fun Frame.firebaseImage(): FirebaseVisionImage {
        val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(this.format)
            .setRotation(this.rotation.rotation())
            .setHeight(this.size.height)
            .setWidth(this.size.width)
            .build()
        return FirebaseVisionImage.fromByteArray(this.data, metadata)
    }

    private fun PictureResult.firebaseImage(): FirebaseVisionImage {
        val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(this.format)
            .setRotation(this.rotation.rotation())
            .setWidth(this.size.width)
            .setHeight(this.size.height)
            .build()
        return FirebaseVisionImage.fromByteArray(this.data, metadata)
    }

    private fun Int.rotation(): Int {
        return when (this) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> FirebaseVisionImageMetadata.ROTATION_0
        }
    }
}