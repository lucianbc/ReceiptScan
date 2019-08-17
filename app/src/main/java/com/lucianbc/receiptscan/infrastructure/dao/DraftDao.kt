package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.*
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import com.lucianbc.receiptscan.domain.usecase.ManageReceiptUseCase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(draft: ReceiptEntity): Single<Long>

    @Update
    fun update(draft: ReceiptEntity): Completable

    @Insert
    fun insert(ocrElements: List<OcrElement>): Single<List<Long>>

    @Query("select id, creationTimestamp from receipt where isDraft == 1 order by creationTimestamp desc")
    fun getDraftItems(): Flowable<List<ListDraftsUseCase.DraftItem>>

    @Query("select imagePath from receipt where id = :id")
    fun getImagePath(id: Long): Flowable<String>

    @Query("select merchantName, date, currency, category, total, id, imagePath, creationTimestamp from receipt where id = :id")
    @Transaction
    fun getDraft(id: Long): Flowable<DraftWithProducts>

    @Query("select id, merchantName, date, total, currency, category, imagePath from receipt where id = :id")
    @Transaction
    fun getReceipt(id: Long): Flowable<ManageReceiptUseCase.Value>

    @Query("select * from ocrElement where receiptId = :receiptId")
    fun getOcrElements(receiptId: Long): Flowable<List<OcrElement>>

    @Query("delete from receipt where id = :receiptId")
    fun delete(receiptId: Long)

    @Query("delete from productDraft where id = :productId")
    fun deleteProduct(productId: Long): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<Product>): Single<List<Long>>

    @Query("update receipt set isDraft = 0 where id = :draftId")
    fun validate(draftId: Long)

    @Query("select id, merchantName, total from receipt where isDraft == 0 order by date desc")
    fun getReceiptItems(): Flowable<List<ListReceiptsUseCase.Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product): Single<Long>

    @Query("select id, merchantName, date, total, currency, category, imagePath from receipt where id = :id")
    fun getExported(id: Long): Single<ExportedReceipt>
}