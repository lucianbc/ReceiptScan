package com.lucianbc.receiptscan.domain.usecase

import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ManageDraftUseCase(
    val draftId: Long,
    val value: Flowable<DraftWithProducts>,
    val repository: DraftsRepository
) {
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

    class Factory @Inject constructor (
        private val draftsRepository: DraftsRepository
    ) {
        fun fetch(draftId: Long): ManageDraftUseCase {
            val value = draftsRepository.getReceipt(draftId)
            return ManageDraftUseCase(draftId, value.cache(), draftsRepository)
        }
    }
}