package com.lucianbc.receiptscan.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "productDraft",
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
data class ProductDraft (
    override val name: String,
    override val price: Float,
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var draftId: Long? = null
) : Product