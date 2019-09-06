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
        val imageStream = context.assets.open("resizedReceipt.jpg")
        return BitmapFactory.decodeStream(imageStream)
    }
}