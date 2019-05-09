package com.lucianbc.receiptscan.domain.model

import androidx.room.*

@Entity(
    tableName = "scan_info_box",
    foreignKeys = [
        ForeignKey(
            entity = ReceiptDraft::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["draft_id"]
        )
    ],
    indices = [Index("draft_id")]
)
data class ScanInfoBox(
    @ColumnInfo(name = "top")           var top     : Int,
    @ColumnInfo(name = "bottom")        var bottom  : Int,
    @ColumnInfo(name = "left")          var left    : Int,
    @ColumnInfo(name = "right")         var right   : Int,
    @ColumnInfo(name = "value")         var value   : String,
    @PrimaryKey(autoGenerate = true)    var id      : ID = null,
    @ColumnInfo(name = "draft_id")      var draftId : ID = null
) {
    constructor(): this(0,0,0,0,"")
}
