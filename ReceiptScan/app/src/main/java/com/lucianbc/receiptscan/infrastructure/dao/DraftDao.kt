package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.*
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.DraftItem
import com.lucianbc.receiptscan.domain.model.ReceiptDraftWithProducts
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(draft: Draft): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(annotations: List<Annotation>): Single<List<Long>>

    @Query("select id, creationTimestamp from draft")
    fun getDraftItems(): Flowable<List<DraftItem>>

    @Query("select imagePath from draft where id = :id")
    fun getImagePath(id: Long): Flowable<String>

    @Query("select merchantName, date, currency, total, id from draft")
    @Transaction
    fun getReceipt(): Flowable<ReceiptDraftWithProducts>

    @Query("select * from annotation where draftId = :draftId")
    fun getAnnotations(draftId: Long): Flowable<List<Annotation>>
}