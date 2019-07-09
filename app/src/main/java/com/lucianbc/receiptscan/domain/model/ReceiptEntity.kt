package com.lucianbc.receiptscan.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "receipt")
data class ReceiptEntity(
    val imagePath: String,
    val merchantName: String?,
    val date: Date?,
    val total: Float?,
    val currency: Currency?,
    val creationTimestamp: Date,
    val isDraft: Boolean,
    @PrimaryKey
    val id: Long? = null
)