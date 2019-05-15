package com.lucianbc.receiptscan.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucianbc.receiptscan.domain.model.Drafts
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanAnnotations

@Dao
@Suppress("FunctionName")
abstract class ReceiptDraftDao {

    fun find(id: ID): Response<ReceiptDraft> {
        val result = _find(id)
        return result.map {
            withAnnotations(it)
        }
    }

    fun insert(receiptDraft: ReceiptDraft): ID {
        val id = _insert(receiptDraft)
        _insertScanAnnotations(receiptDraft.annotations)
        return id
    }

    @Query("SELECT * FROM receipt_draft")
    abstract fun findAll(): Response<Drafts>

    @Query("SELECT * FROM receipt_draft WHERE id = :draftId")
    abstract fun _find(draftId: ID): Response<ReceiptDraft>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun _insert(receiptDraft: ReceiptDraft): ID

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun _insertScanAnnotations(infoBoxes: ScanAnnotations): List<ID>

    @Query("SELECT * FROM scan_info_box WHERE draft_id = :receiptId")
    abstract fun _findAnnotations(receiptId: ID): ScanAnnotations

    private fun withAnnotations(receiptDraft: ReceiptDraft): ReceiptDraft {
        val annotations = _findAnnotations(receiptDraft.id)
        return ReceiptDraft(receiptDraft.imagePath, annotations, receiptDraft.id)
    }
}