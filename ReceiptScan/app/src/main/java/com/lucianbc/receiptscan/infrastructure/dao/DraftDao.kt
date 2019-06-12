package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.*
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

@Dao
interface DraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(draft: ReceiptEntity): Single<Long>

    @Insert
    fun insert(ocrElements: List<OcrElement>): Single<List<Long>>

    @Query("select id, creationTimestamp from receipt where isDraft == 1 order by creationTimestamp desc")
    fun getDraftItems(): Flowable<List<ListDraftsUseCase.DraftItem>>

    @Query("select imagePath from receipt where id = :id")
    fun getImagePath(id: Long): Flowable<String>

    @Query("select merchantName, date, currency, total, id from receipt where id = :id")
    @Transaction
    fun getReceipt(id: Long): Flowable<DraftWithProducts>

    @Query("select * from ocrElement where receiptId = :receiptId")
    fun getOcrElements(receiptId: Long): Flowable<List<OcrElement>>

    @Query("delete from receipt where id = :receiptId")
    fun delete(receiptId: Long)

    @Query("update receipt set merchantName = :merchantName, date = :date, currency = :currency, total = :total where id = :receiptId")
    fun updateReceipt(merchantName: String?, date: Date?, currency: Currency?, total: Float?, receiptId: Long)

    @Query("delete from productDraft where receiptId = :receiptId")
    fun deleteProducts(receiptId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<Product>): Single<List<Long>>

    @Transaction
    fun updateReceipt(data: DraftWithProducts) {
        updateReceipt(data.receipt.merchantName, data.receipt.date, data.receipt.currency, data.receipt.total, data.receipt.id)
        deleteProducts(data.receipt.id)
        insertProducts(data.products)
    }

    @Query("update receipt set isDraft = 0 where id = :draftId")
    fun validate(draftId: Long)

    @Query("select id, merchantName, total from receipt where isDraft == 0 order by date desc")
    fun getReceiptItems(): Flowable<List<ListReceiptsUseCase.Item>>
}