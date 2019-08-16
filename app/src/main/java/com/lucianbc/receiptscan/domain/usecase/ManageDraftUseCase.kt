package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.model.SharingOption
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.ReceiptSender
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ManageDraftUseCase(
    private val draftId: Long,
    val value: Flowable<DraftWithProducts>,
    private val repository: DraftsRepository,
    private val sharingOption: SharingOption,
    private val sender: ReceiptSender
) {
    val image by lazy { repository.getImage(draftId) }

    fun <T> update(newVal: T, mapper: ((T, DraftWithProducts) -> Draft)) {
        updateSubs(newVal, mapper).subscribe()
    }

    fun <T> updateSubs(newVal: T, mapper: ((T, DraftWithProducts) -> Draft)) =
        Observable.just(newVal)
            .withLatestFrom(value.toObservable())
            .flatMap {
                mapper
                    .invoke(newVal, it.second)
                    .run {
                        if (this == it.second.receipt)
                            Observable.empty<Draft>()
                        else
                            Observable.just(this)
                    }
            }
            .flatMapCompletable { repository.update(it) }
            .subscribeOn(Schedulers.io())


    fun createProduct(): Single<Product> {
        val newProd = Product("", 0f, receiptId = draftId)
        return repository
            .insert(newProd)
            .map { newProd.apply { this.id = it } }
            .subscribeOn(Schedulers.io())
    }

    fun updateProduct(product: Product) {
        repository
            .insert(product)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun deleteProduct(it: Product) =
        repository
            .deleteProduct(it.id!!)
            .subscribeOn(Schedulers.io())

    fun deleteDraft() =
        Observable
            .fromCallable { repository.delete(draftId) }

    fun validateDraft(): Completable =
        Completable
            .fromCallable { repository.validate(draftId) }
            .andThen(sendReceiptComputation)

    private val sendReceiptComputation = Completable.defer {
        if (!sharingOption.enabled)
            Completable.complete()
        else
            Completable.fromCallable { sender.send(draftId) }
    }

    class Factory @Inject constructor (
        private val draftsRepository: DraftsRepository,
        private val sharingOption: SharingOption,
        private val sender: ReceiptSender
    ) {
        fun fetch(draftId: Long): ManageDraftUseCase {
            val value = draftsRepository
                .getDraft(draftId)
                .replay(1)
                .autoConnect()
            return ManageDraftUseCase(draftId, value, draftsRepository, sharingOption, sender)
        }
    }
}