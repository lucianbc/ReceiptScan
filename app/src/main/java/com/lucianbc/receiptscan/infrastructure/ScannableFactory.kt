package com.lucianbc.receiptscan.infrastructure

import android.graphics.Bitmap
import com.google.android.gms.tasks.Tasks
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.domain.viewfinder.OcrElementValue
import com.lucianbc.receiptscan.domain.viewfinder.OcrElements
import com.lucianbc.receiptscan.domain.viewfinder.Scannable
import com.otaliastudios.cameraview.Frame
import com.otaliastudios.cameraview.PictureResult
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ScannableFactory @Inject constructor(
    private val recognizer: FirebaseVisionTextRecognizer
) {
    fun create(frame: Frame): Scannable {
        return object : Scannable {
            val firebaseImage = frame.firebaseImage()
            override fun ocrElements() =
                firebaseImage.ocrElements()

            override fun image() =
                Observable.just(firebaseImage.bitmap)
        }
    }

    fun create(image: Bitmap): Scannable {
        return object: Scannable {
            val fib = image.firebaseImage()
            override fun ocrElements() = fib.ocrElements()

            override fun image() =
                Observable.just(fib.bitmap)
        }
    }

    fun create(pictureResult: PictureResult): Scannable {
        val firebaseImage = pictureResult.firebaseImage()
        return object: Scannable {
            override fun ocrElements() = firebaseImage
                .flatMap { it.ocrElements() }

            override fun image() = firebaseImage
                .map { it.bitmap }
        }
    }

    fun create(imageSource: Observable<Bitmap>): Scannable {
        return object: Scannable {
            override fun ocrElements() = imageSource.flatMap {
                it.firebaseImage().ocrElements()
            }

            override fun image(): Observable<Bitmap> =
                imageSource
        }
    }

    private fun FirebaseVisionImage.ocrElements(): Observable<OcrElements> =
        Observable.fromCallable {
            try {
                val result = Tasks.await(recognizer.processImage(this))
                result
                    .textBlocks
                    .flatMap { it.lines }
                    .asSequence()
                    .mapNotNull { it.toOcrElement() }
            } catch (e: InterruptedException) {
                emptySequence<OcrElementValue>()
            }
        }

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
        val result = BehaviorSubject.create<FirebaseVisionImage>()

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
