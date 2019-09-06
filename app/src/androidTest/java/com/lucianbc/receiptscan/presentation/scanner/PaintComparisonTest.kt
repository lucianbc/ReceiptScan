package com.lucianbc.receiptscan.presentation.scanner

import android.graphics.*
import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.lucianbc.receiptscan.domain.extract.OcrElements
import com.lucianbc.receiptscan.domain.extract.rules.RawReceipt
import com.lucianbc.receiptscan.infrastructure.ScannableFactory
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

@RunWith(AndroidJUnit4::class)
class PaintComparisonTest {

    @Test
    fun paintMy() {
        val image = readImage()
        val recognizer = FirebaseVision.getInstance().onDeviceTextRecognizer

        val sf = ScannableFactory(recognizer)
        val oocrEls = sf.create(image).ocrElements()

        val ocrEls = oocrEls.blockingSingle()

        val bmp = paintOcrEls(ocrEls, image)

        saveBmp(bmp!!, "myBlocks.jpg")
    }

    @Test
    fun paintFib() {
        val image = readImage()
        val recognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        val firebaseImage = FirebaseVisionImage.fromBitmap(image)
        val result = recognizer.processImage(firebaseImage).let { Tasks.await(it) }
        val newBmp = paintFirebaseVision(result, firebaseImage.bitmap)
        saveBmp(newBmp!!, "fibBlocks.jpg")
    }

    private fun saveBmp(bmp: Bitmap, fileName: String) {
        val fos = FileOutputStream(File(
            Environment.getExternalStorageDirectory().toString(),
            fileName
        ))
        fos.use {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.flush()
        }
    }

    private fun paintOcrEls(result: OcrElements, img: Bitmap): Bitmap? {
        val bitmap = img.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)
        val rr = RawReceipt.create(result)

        rr.forEach {
            val rect = Rect(it.left, it.top, it.right, it.bottom)
            canvas.drawRect(rect, LINE_PAINT)
        }

        rr.flatMap { it.elements }.forEach {
            val rect = Rect(it.left, it.top, it.right, it.bottom)
            canvas.drawRect(rect, ELEMENT_PAINT)
        }

        return bitmap
    }

    private fun paintFirebaseVision(result: FirebaseVisionText, img: Bitmap): Bitmap? {
        val bitmap = img.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)
        result.textBlocks.forEach {
            canvas.drawRect(it.boundingBox!!, BLOCKS_PAINT)
        }
        result.textBlocks.flatMap { it.lines }.forEach {
            canvas.drawRect(it.boundingBox!!, LINE_PAINT)
        }
        result.textBlocks.flatMap { it.lines }
            .flatMap { it.elements }
            .forEach {
                canvas.drawRect(it.boundingBox!!, ELEMENT_PAINT)
            }
        return bitmap
    }

    private fun readImage(): Bitmap {
        val context = InstrumentationRegistry.getInstrumentation().context
        val imageStream = context.assets.open("receipt.jpg")
        return BitmapFactory.decodeStream(imageStream).rotate(90f)
    }

    private fun Bitmap.rotate(deg: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(deg)
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    companion object {
        private val BLOCKS_PAINT = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.BLUE
            strokeWidth = 10f
        }

        private val LINE_PAINT = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.GREEN
            strokeWidth = 12f
        }

        private val ELEMENT_PAINT = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = 14f
        }
    }
}