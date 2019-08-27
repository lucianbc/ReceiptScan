package com.lucianbc.receiptscan.infrastructure.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lucianbc.receiptscan.domain.model.Category
import java.util.*

@Entity(tableName = "receipt")
data class ReceiptEntity(
    val imagePath: String,
    val merchantName: String?,
    val date: Date?,
    val total: Float?,
    val currency: Currency?,
    val category: Category,
    val creationTimestamp: Date,
    val isDraft: Boolean,
    @PrimaryKey
    val id: Long? = null
)