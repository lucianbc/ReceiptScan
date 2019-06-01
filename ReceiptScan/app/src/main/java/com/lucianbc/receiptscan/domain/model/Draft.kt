package com.lucianbc.receiptscan.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "draft")
data class Draft (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val imagePath: String,
    val merchantName: String?,
    val date: Date?,
    val total: Float?,
    val currency: Currency,
    val isLinked: Boolean,
    val creationTimestamp: Date
)