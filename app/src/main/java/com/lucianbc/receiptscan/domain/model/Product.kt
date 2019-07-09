package com.lucianbc.receiptscan.domain.model

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
data class Product(
    val name: String,
    val price: Float,
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var receiptId: Long? = null
)