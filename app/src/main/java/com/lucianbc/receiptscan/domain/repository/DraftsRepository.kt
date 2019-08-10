package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.model.OcrElement
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.scanner.DraftValue
import com.lucianbc.receiptscan.domain.usecase.ListDraftsUseCase
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface DraftsRepository {
    fun create(value: DraftValue): Observable<Long>
    fun insert(draft: Draft): Single<Long>
    fun update(product: Product): Single<Long>
    fun insert(product: Product): Single<Long>
    fun getAllItems(): Flowable<List<ListDraftsUseCase.DraftItem>>
    fun getAllReceiptItems(): Flowable<List<ListReceiptsUseCase.Item>>
    fun getImage(id: Long): Flowable<Bitmap>
    fun getDraft(id: Long): Flowable<DraftWithProducts>
    fun getOcrElements(draftId: Long): Flowable<List<OcrElement>>
    fun delete(draftId: Long)
    fun validate(draftId: Long)
    fun deleteProduct(id: Long): Completable
    fun update(draft: Draft): Completable
}