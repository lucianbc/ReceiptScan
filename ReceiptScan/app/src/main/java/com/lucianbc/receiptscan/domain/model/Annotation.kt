package com.lucianbc.receiptscan.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "annotation",
    foreignKeys = [
        ForeignKey(
            entity = Draft::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["draftId"]
        )
    ],
    indices = [Index("draftId")]
)
data class Annotation (
    val text: String,
    val top: Int,
    val bottom: Int,
    val left: Int,
    val right: Int,
    val tag: Tag,
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var draftId: Long? = null
)