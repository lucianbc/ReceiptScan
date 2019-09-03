interface ExportUseCase {
    fun list(): Flowable<List<Export>>
    fun newExport(manifest: Session): Completable
    fun markAsFinished(notification: FinishedNotification): Completable
}

data class Session(
    val firstDate: Date,
    val lastDate: Date,
    val content: Content,
    val format: Format,
    val id: String
) : Parcelable {

    enum class Content {
        TextOnly,
        TextAndImage
    }

    enum class Format {
        JSON,
        CSV
    }
}

data class FinishedNotification(
    val exportId: String,
    val downloadUrl: String
)