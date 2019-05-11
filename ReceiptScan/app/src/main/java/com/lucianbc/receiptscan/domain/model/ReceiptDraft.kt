package com.lucianbc.receiptscan.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "receipt_draft")
data class ReceiptDraft(
    @ColumnInfo(name="image_path")  var imagePath   : String,
    @Ignore                         var annotations : ScanAnnotations = Collections.emptyList(),
    @PrimaryKey(autoGenerate=true)  var id          : ID = null
) {
    constructor(): this("")
}

