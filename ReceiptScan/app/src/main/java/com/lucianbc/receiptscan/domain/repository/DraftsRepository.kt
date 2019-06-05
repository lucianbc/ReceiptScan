package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.domain.model.CreateDraftCommand
import com.lucianbc.receiptscan.domain.model.DraftItem
import com.lucianbc.receiptscan.domain.model.ReceiptDraftWithProducts
import io.reactivex.Flowable
import io.reactivex.Observable

interface DraftsRepository {
    fun create(command: CreateDraftCommand): Observable<Long>
    fun getAllItems(): Flowable<List<DraftItem>>
    fun getImage(id: Long): Flowable<Bitmap>
    fun getReceipt(id: Long): Flowable<ReceiptDraftWithProducts>
    fun getAnnotations(draftId: Long): Flowable<List<Annotation>>
    fun delete(draftId: Long)
    fun editAnnotation(newAnnotation: Annotation)
    fun saveReceipt(data: ReceiptDraftWithProducts)
}