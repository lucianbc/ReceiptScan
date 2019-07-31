package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ManageDraftUseCase(
    private val draftId: Long,
    val value: Flowable<DraftWithProducts>,
    private val repository: DraftsRepository
) {
    val image = repository.getImage(draftId)

    fun <T> update(newVal: T, mapper: ((T, DraftWithProducts) -> Draft)) {
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
            .flatMapSingle { repository.update(it) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

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


    class Factory @Inject constructor (
        private val draftsRepository: DraftsRepository
    ) {
        fun fetch(draftId: Long): ManageDraftUseCase {
            val value = draftsRepository.getReceipt(draftId)
            return ManageDraftUseCase(draftId, value.cache(), draftsRepository)
        }
    }
}