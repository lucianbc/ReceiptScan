package com.lucianbc.receiptscan.infrastructure.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lucianbc.receiptscan.domain.export.Session
import com.lucianbc.receiptscan.domain.export.Status
import java.util.*

@Entity(
    tableName = "export"
)
data class ExportEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val firstDate: Date,
    val lastDate: Date,
    val content: Session.Content,
    val format: Session.Format,
    val status: Status,
    val downloadLink: String
)