package com.lucianbc.receiptscan.domain.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucianbc.receiptscan.domain.model.Drafts
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanAnnotations

@Dao
abstract class ReceiptDraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertScanAnnotations(infoBoxes: ScanAnnotations)

    @Query("SELECT * FROM scan_info_box WHERE draft_id = :receiptId")
    abstract fun findAnnotations(receiptId: ID): ScanAnnotations

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(receiptDraft: ReceiptDraft)

    fun insertWithAnnotations(receiptDraft: ReceiptDraft) {
        insert(receiptDraft)
        insertScanAnnotations(receiptDraft.annotations)
    }

    @Query("SELECT * FROM receipt_draft")
    abstract fun findAll(): LiveData<Drafts>

    fun findAllWithAnnotations(): LiveData<Drafts> {
        val drafts = findAll()

        return Transformations.map<Drafts, Drafts>(drafts) {
            it.map { d ->
                withAnnotations(d)
            }
        }
    }

    private fun withAnnotations(receiptDraft: ReceiptDraft): ReceiptDraft {
        val annotations = findAnnotations(receiptDraft.id)
        return ReceiptDraft(receiptDraft.imagePath, annotations, receiptDraft.id)
    }
}