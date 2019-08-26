package com.lucianbc.receiptscan.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ocrElement",
    foreignKeys = [
        ForeignKey(
            entity = ReceiptEntity::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["receiptId"]
        )
    ],
    indices = [Index("receiptId")]
)
data class OcrElementEntity(
    val text: String,
    val top: Int,
    val left: Int,
    val bottom: Int,
    val right: Int,
    val receiptId: Long? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)
