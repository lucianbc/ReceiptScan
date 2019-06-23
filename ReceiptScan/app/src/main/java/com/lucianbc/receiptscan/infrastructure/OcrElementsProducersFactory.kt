package com.lucianbc.receiptscan.infrastructure

import android.graphics.Bitmap
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.domain.scanner.OcrElementValue
import com.lucianbc.receiptscan.domain.scanner.OcrElements
import com.lucianbc.receiptscan.domain.scanner.FrameProducer
import com.lucianbc.receiptscan.domain.scanner.OcrWithImageProducer
import com.otaliastudios.cameraview.Frame
import com.otaliastudios.cameraview.PictureResult
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class OcrElementsProducersFactory @Inject constructor(
    private val recognizer: FirebaseVisionTextRecognizer
) {
    fun simple(frame: Frame): FrameProducer {
        return object : FrameProducer {
            override fun produce(): Observable<OcrElements> =
                frame
                    .firebaseImage()
                    .let { Observable.just(it).flatMap { fib -> fib.recognize() }
            }
        }
    }

    fun withImage(image: PictureResult): OcrWithImageProducer {
        return object : OcrWithImageProducer {
            override fun produce(): Observable<Pair<Bitmap, OcrElements>> =
                image.firebaseImage()
                    .map { it.bitmap to it }
                    .flatMap { it.ocrElements() }
                    .subscribeOn(Schedulers.computation())
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


    private fun FirebaseVisionText.Line.toOcrElement(): OcrElementValue? =
        if (this.boundingBox != null)
            OcrElementValue(
                this.text,
                this.boundingBox!!.top,
                this.boundingBox!!.left,
                this.boundingBox!!.bottom,
                this.boundingBox!!.right
            )
        else null

    private fun Bitmap.firebaseImage() = FirebaseVisionImage.fromBitmap(this)

    private fun PictureResult.firebaseImage(): Observable<FirebaseVisionImage> {
        val result = PublishSubject.create<FirebaseVisionImage>()

        this.toBitmap {
            if (it != null) {
                result.onNext(it.firebaseImage())
                result.onComplete()
            }
            else
                result.onError(IllegalArgumentException("Picture could not be converted to bitmap"))
        }

        return result
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

    private fun Frame.firebaseImage(): FirebaseVisionImage {
        val metadata = FirebaseVisionImageMetadata.Builder()
            .setFormat(this.format)
            .setRotation(this.rotation.rotation())
            .setHeight(this.size.height)
            .setWidth(this.size.width)
            .build()
        return FirebaseVisionImage.fromByteArray(this.data, metadata)
    }
}
