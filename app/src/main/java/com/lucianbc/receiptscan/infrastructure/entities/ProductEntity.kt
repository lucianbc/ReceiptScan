package com.lucianbc.receiptscan.infrastructure.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "productDraft",
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
data class ProductEntity(
    var name: String,
    var price: Float,
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var receiptId: Long? = null
)