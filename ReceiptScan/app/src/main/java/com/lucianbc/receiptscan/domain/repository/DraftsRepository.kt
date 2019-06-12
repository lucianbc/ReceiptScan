package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import io.reactivex.Flowable
import io.reactivex.Observable

interface DraftsRepository {
    fun create(value: DraftValue): Observable<Long>
    fun getAllItems(): Flowable<List<ListDraftsUseCase.DraftItem>>
    fun getAllReceiptItems() : Flowable<List<ListReceiptsUseCase.Item>>
    fun getImage(id: Long): Flowable<Bitmap>
    fun getReceipt(id: Long): Flowable<DraftWithProducts>
    fun getOcrElements(draftId: Long): Flowable<List<OcrElement>>
    fun delete(draftId: Long)
    fun saveReceipt(data: DraftWithProducts)
    fun validate(draftId: Long)
}