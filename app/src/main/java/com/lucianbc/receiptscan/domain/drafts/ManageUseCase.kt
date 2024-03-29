package com.lucianbc.receiptscan.domain.drafts

import com.lucianbc.receiptscan.domain.extract.DraftId
import com.lucianbc.receiptscan.domain.collect.CollectingOption
import com.lucianbc.receiptscan.domain.collect.ReceiptCollector
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ManageUseCase @AssistedInject constructor(
    @Assisted private val draftId: DraftId,
    @Assisted private val source: Flowable<Draft>,
    private val repository: DraftsRepository,
    private val collectingOption: CollectingOption,
    private val collector: ReceiptCollector
) : DraftsUseCase.Manage {

    override val image by lazy {
        repository.getImage(draftId).subscribeOn(Schedulers.computation())
    }

    // Since I am using a custom query, I do not get the updates from room.
    // TODO: Look more into this problem
    // At the moment, I am updating those values, but the products are not updated.
    private val _value = BehaviorSubject.create<Draft>()

    override val value: Flowable<Draft> = _value
        .mergeWith(source.toObservable())
        .toFlowable(BackpressureStrategy.LATEST)
        .replay(1)
        .autoConnect()

    override fun <T> update(newVal: T, mapper: (T, Draft) -> Draft): Completable {
        return Observable.just(newVal)
            .zipWith(value.toObservable())
            .flatMap {
                mapper(newVal, it.second)
                    .run {
                        if (this == it.second)
                            Observable.empty<Draft>()
                        else
                            Observable.just(this)
                    }
            }
            .flatMapCompletable {
                _value.onNext(it)
                repository.update(it)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun delete() =
        repository.delete(draftId)
            .subscribeOn(Schedulers.io())

    override fun moveToValid(): Completable =
        repository.moveDraftToValid(draftId)
            .andThen(sendReceiptComputation)
            .subscribeOn(Schedulers.io())

    override fun createProduct() =
        repository.addEmptyProductTo(draftId).subscribeOn(Schedulers.io())

    override fun updateProduct(product: Product) =
        repository.updateProductIn(product, draftId).subscribeOn(Schedulers.io())

    override fun deleteProduct(product: Product) =
        repository.deleteProduct(product)
            .subscribeOn(Schedulers.io())

    private val sendReceiptComputation = Completable.defer {
        when {
            collectingOption.enabled -> Completable.fromCallable { collector.send(draftId) }
            else -> Completable.complete()
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(draftId: DraftId, source: Flowable<Draft>): ManageUseCase
    }
}
