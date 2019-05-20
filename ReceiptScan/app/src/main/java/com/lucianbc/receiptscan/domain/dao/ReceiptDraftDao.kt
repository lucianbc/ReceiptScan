package com.lucianbc.receiptscan.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucianbc.receiptscan.domain.model.*
import io.reactivex.Single

@Dao
@Suppress("FunctionName")
abstract class ReceiptDraftDao {

    fun find(id: ID): Response<ReceiptDraft> {
        val result = _find(id)
        return withAnnotations(result)
    }

    fun insert(receiptDraft: ReceiptDraft): ID {
        val id = _insert(receiptDraft)
        receiptDraft.annotations.forEach { it.draftId = id }
        _insertScanAnnotations(receiptDraft.annotations)
        return id
    }

    @Query("SELECT * FROM receipt_draft ORDER BY receipt_draft.id DESC")
    abstract fun findAll(): Response<Drafts>

    @Query("SELECT * FROM receipt_draft WHERE id = :draftId")
    abstract fun _find(draftId: ID): Response<ReceiptDraft>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun _insert(receiptDraft: ReceiptDraft): ID

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun _insertScanAnnotations(infoBoxes: ScanAnnotations): List<ID>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAnnotation(scanInfoBox: ScanInfoBox): Single<ID>

    @Query("SELECT * FROM scan_info_box WHERE draft_id = :receiptId")
    abstract fun _findAnnotations(receiptId: ID): Response<ScanAnnotations>

    private fun withAnnotations(receiptDraft: Response<ReceiptDraft>): Response<ReceiptDraft> =
        receiptDraft.flatMap { draft ->
            _findAnnotations(draft.id).map {
                    ans -> ReceiptDraft(draft.imagePath, ans, draft.id)
            }
        }
}