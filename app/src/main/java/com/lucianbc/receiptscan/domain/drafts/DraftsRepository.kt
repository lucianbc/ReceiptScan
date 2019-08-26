package com.lucianbc.receiptscan.domain.drafts

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.extract.DraftId
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface DraftsRepository {
    fun listDrafts(): Flowable<List<DraftListItem>>
    fun getDraft(draftId: DraftId): Flowable<Draft>
    fun getImage(draftId: DraftId): Flowable<Bitmap>
    fun update(draft: Draft): Completable
    fun addEmptyProductTo(draftId: DraftId): Single<Product>
    fun updateProductIn(product: Product, draftId: DraftId): Completable
    fun deleteProduct(product: Product) : Completable
    fun delete(draftId: DraftId): Completable
    fun moveDraftToValid(draftId: DraftId): Completable
}