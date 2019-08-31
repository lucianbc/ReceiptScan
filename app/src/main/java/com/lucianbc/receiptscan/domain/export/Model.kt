package com.lucianbc.receiptscan.domain.export

import android.os.Parcelable
import androidx.room.Relation
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.infrastructure.entities.ProductEntity
import kotlinx.android.parcel.Parcelize
import java.util.*

@Suppress("DataClassPrivateConstructor")
@Parcelize
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

    companion object {
        @Suppress("LocalVariableName")
        fun validate(firstDate: Date?, lastDate: Date?, content: Content?, format: Format?): Session {
            val _firstDate = firstDate ?: throw ExportException.Cause.BAD_RANGE()
            val _lastDate = lastDate ?: throw ExportException.Cause.BAD_RANGE()
            val _content = content ?: throw ExportException.Cause.BAD_CONTENT()
            val _format = format ?: throw ExportException.Cause.BAD_FORMAT()

            if (_firstDate > _lastDate) throw ExportException.Cause.BAD_RANGE()

            return Session(
                _firstDate,
                _lastDate,
                _content,
                _format,
                UUID.randomUUID().toString()
            )
        }
    }
}

enum class Status {
    UPLOADING,
    WAITING_DOWNLOAD,
    COMPLETE
}

data class Export (
    val id: String,
    val firstDate: Date,
    val lastDate: Date,
    val content: Session.Content,
    val format: Session.Format,
    val status: Status,
    val downloadLink: String
)

data class FinishedNotification(
    val exportId: String,
    val downloadUrl: String
)

data class Manifest (
    val firstDate: Date,
    val lastDate: Date,
    val content: Session.Content,
    val format: Session.Format,
    val id: String,
    val notificationToken: String
) {
    constructor(session: Session, notificationToken: String) : this(
        session.firstDate,
        session.lastDate,
        session.content,
        session.format,
        session.id,
        notificationToken
    )
}

data class TextReceipt (
    val id: Long,
    val merchantName: String,
    val date: Date,
    val total: Float,
    val currency: Currency,
    val category: Category,
    @Relation(parentColumn = "id", entityColumn = "receiptId")
    val productEntities: List<ProductEntity>
)

data class ImageReceipt (
    val id: Long,
    val merchantName: String,
    val date: Date,
    val total: Float,
    val currency: Currency,
    val category: Category,
    val imagePath: String,
    @Relation(parentColumn = "id", entityColumn = "receiptId")
    val productEntities: List<ProductEntity>
)