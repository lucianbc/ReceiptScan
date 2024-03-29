package com.lucianbc.receiptscan.presentation.scanner

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OcrCompareTest {

    @Test
    fun runOcr() {
        val image = readImage()
        val recognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        val firebaseImage = FirebaseVisionImage.fromBitmap(image)
        val result = recognizer.processImage(firebaseImage).let { Tasks.await(it) }
        println(result)
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
}