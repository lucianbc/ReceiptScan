package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.*
import com.lucianbc.receiptscan.domain.drafts.DraftListItem
import com.lucianbc.receiptscan.domain.extract.DraftId
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.receipts.Receipt
import com.lucianbc.receiptscan.domain.receipts.ReceiptId
import com.lucianbc.receiptscan.domain.receipts.ReceiptListItem
import com.lucianbc.receiptscan.presentation.home.exports.ExportUseCase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(draft: ReceiptEntity): Single<Long>

    @Insert
    fun insert(ocrElementEntities: List<OcrElementEntity>): Single<List<Long>>

    @Query("select imagePath from receipt where id = :id")
    fun getImagePath(id: Long): Flowable<String>

    @Query("delete from receipt where id = :receiptId")
    fun delete(receiptId: Long): Completable

    @Query("delete from productDraft where id = :productId")
    fun deleteProduct(productId: Long): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(productEntities: List<ProductEntity>): Single<List<Long>>

    @Query("update receipt set isDraft = 0 where id = :draftId")
    fun validate(draftId: Long): Completable

    @Query("""
        select  id, merchantName, total 
        from    receipt 
        where   isDraft == 0 
        order   by date desc
    """)
    fun getReceiptItems(): Flowable<List<ReceiptListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productEntity: ProductEntity): Single<Long>

    @Query("""
        select  id, merchantName, date, total, currency, category, imagePath 
        from    receipt 
        where   id = :id
    """)
    fun getExported(id: Long): Single<ExportedReceipt>

    @Query("""
        select  id, merchantName, date, total, currency, category 
        from    receipt 
        where   date between :firstDate and :lastDate
    """)
    fun getTextReceiptsBetween(firstDate: Long, lastDate: Long): Single<ExportUseCase.TextReceipt>

    @Query("""
        select  id, merchantName, date, total, currency, category, imagePath 
        from    receipt 
        where   date between :firstDate and :lastDate
    """)
    fun getImageReceiptsBetween(firstDate: Long, lastDate: Long): Single<ExportUseCase.ImageReceipt>

    @Query(
        """
        select  id, creationTimestamp 
        from    receipt 
        where   isDraft == 1 
        order   by creationTimestamp desc
    """
    )
    fun listDrafts(): Flowable<List<DraftListItem>>

    @Query("select * from receipt where id == :id and isDraft == 1")
    fun selectDraft(id: DraftId): Flowable<ReceiptEntity>

    @Query("select * from receipt where id == :id and isDraft == 0")
    fun selectReceipt(id: ReceiptId): Flowable<ReceiptEntity>

    @Query("select * from productDraft where receiptId == :draftId")
    fun selectProducts(draftId: DraftId): Flowable<List<ProductEntity>>

    @Query("select * from ocrElement where receiptId == :draftId")
    fun selectOcrElements(draftId: DraftId): Flowable<List<OcrElementEntity>>

    @Query("""
        update  receipt
        set     merchantName    = :merchantName,
                date            = :date,
                total           = :total,
                currency        = :currency,
                category        = :category
        where   id == :id
    """)
    fun updateDraft(
        merchantName: String?,
        date: Date?,
        total: Float?,
        currency: Currency?,
        category: Category,
        id: DraftId
    ) : Completable
}