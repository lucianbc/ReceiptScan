package com.lucianbc.receiptscan.domain.repository

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import com.lucianbc.receiptscan.domain.usecase.ManageReceiptUseCase
import com.lucianbc.receiptscan.presentation.home.exports.ExportUseCase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

interface AppRepository {
    fun insert(draft: Draft): Single<Long>
    fun update(product: Product): Single<Long>
    fun insert(product: Product): Single<Long>
    fun getAllReceiptItems(): Flowable<List<ListReceiptsUseCase.Item>>
    fun getImage(id: Long): Flowable<Bitmap>
    fun getDraft(id: Long): Flowable<DraftWithProducts>
    fun getOcrElements(draftId: Long): Flowable<List<OcrElement>>
    fun delete(draftId: Long)
    fun validate(draftId: Long)
    fun deleteProduct(id: Long): Completable
    fun update(draft: Draft): Completable
    fun getReceipt(id: Long): Flowable<ManageReceiptUseCase.Value>
    fun getExported(id: Long): Single<ExportedReceipt>
    fun getTextReceiptsBeteewn(firstDate: Date, lastDate: Date): Single<ExportUseCase.TextReceipt>
    fun getImageReceiptsBetween(firstDate: Date, lastDate: Date): Single<ExportUseCase.ImageReceipt>
}