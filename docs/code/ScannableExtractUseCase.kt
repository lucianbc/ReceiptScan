interface Scannable {
    fun ocrElements(): Observable<OcrElements>
    fun image(): Observable<Bitmap>
}

interface ExtractUseCase {
    val state: Flowable<State>
    val preview: Flowable<OcrElements>
    fun feedPreview(frame: Scannable)
    fun extract(frame: Scannable): Single<DraftId>
}

sealed class State {
    object Processing : State()
    object Idle : State()
    data class Error(val err: Throwable) : State()
}