package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.*
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.model.Annotation
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

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

    @Query("select merchantName, date, currency, total, id from draft where id = :id")
    @Transaction
    fun getReceipt(id: Long): Flowable<ReceiptDraftWithProducts>

    @Query("select * from annotation where draftId = :draftId")
    fun getAnnotations(draftId: Long): Flowable<List<Annotation>>

    @Query("delete from draft where id = :draftId")
    fun delete(draftId: Long)

    @Query("update draft set merchantName = :merchantName, date = :date, currency = :currency, total = :total where id = :draftId")
    fun updateReceipt(merchantName: String?, date: Date?, currency: Currency?, total: Float?, draftId: Long)

    @Query("delete from productDraft where draftId = :draftId")
    fun deleteProducts(draftId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<ProductDraft>)

    @Transaction
    fun updateReceipt(data: ReceiptDraftWithProducts) {
        updateReceipt(data.receipt.merchantName, data.receipt.date, data.receipt.currency, data.receipt.total, data.receipt.id)
        deleteProducts(data.receipt.id)
        insertProducts(data.products)
    }

    @Update
    fun update(newAnnotation: Annotation)
}