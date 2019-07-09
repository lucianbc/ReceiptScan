package com.lucianbc.receiptscan.presentation.scanner

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.lucianbc.receiptscan.domain.viewfinder.LiveViewUseCase
import com.lucianbc.receiptscan.infrastructure.ScannableFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ViewfinderViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: ViewfinderViewModel
    lateinit var factory: ScannableFactory
    lateinit var recognizer: FirebaseVisionTextRecognizer


    @Before
    fun setup() {
        val useCase = LiveViewUseCase(15f)
        recognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        factory = ScannableFactory(recognizer)
        viewModel = ViewfinderViewModel(useCase, factory)
    }

    private fun dummyImageData(): Bitmap {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val bmpStream = ctx.assets.open("resizedReceipt.jpg")
        return BitmapFactory.decodeStream(bmpStream)
    }

    /**
     * This should test that the frames do not buffer up and flood the ocr recognizer.
     * So far, I did not come up with a method to assert on this behavior, but this test
     * is useful during developing and debugging.
     */
    @Test
    fun testThrottle() {
        val lifecycleOwner: LifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(lifecycleOwner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        viewModel.ocrElements
            .observe( { lifecycle }, {
                print("Finished: ")
                println(it.joinToString(" ") { t -> t.text }.subSequence(0, 10))
            })
        for (i in 1..200) {
            val bmp = dummyImageData()
            viewModel.processImage(bmp)
            println("Pushed $i")
        }
        Thread.sleep(1000)
    }
}