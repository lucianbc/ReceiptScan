package com.lucianbc.receiptscan.domain.receipts

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.zipWith
import java.util.*
import javax.inject.Inject

class SourcesManager @Inject constructor(
    repository: ReceiptsRepository
) : ISourcesManager {
    private val currencySource = BehaviorProcessor.create<Currency>()

    private val monthSource = BehaviorProcessor.create<Date>()

    private val spendingGroupSource = BehaviorProcessor.create<SpendingGroup>()

    override val availableCurrencies = repository
        .getAvailableCurrencies()
        .replay(1)
        .autoConnect()

    val currentCurrency = currencySource
        .mergeWith(availableCurrencies.firstOrEmpty())

    override val availableMonths = currentCurrency
        .flatMap {
            repository
                .getAvailableMonths(it)
        }
        .map { it.sortedDescending() }
        .replay(1)
        .autoConnect()

    private val queryMonth = monthSource
        .mergeWith(availableMonths
            .doOnNext {
                println("Here something")
            }
            .firstOrEmpty())
        .replay(1)
        .autoConnect()

    override val categories = currentCurrency
        .doOnNext {
            println("Stuck Here")
        }
//        .withLatestFrom(queryMonth)
        .zipWith(queryMonth)
        .flatMap {
            repository.getAllSpendings(it.first, it.second)
        }
        .map {
            println(it)
            it
        }
        .replay(1)
        .autoConnect()

    override val currentSpending = spendingGroupSource
        .mergeWith(categories.firstOrEmpty())

    override val transactions = currentCurrency
        .doOnNext {
            println("Stuck here I guess")
        }
        .zipWith(queryMonth)
        .zipWith(currentSpending) { t1, t2 -> Triple(t1.first, t1.second, t2) }
        .flatMap {
            when(val type = it.third.group) {
                is Group.Categorized -> repository.getTransactions(it.first, it.second, type.value)
                Group.Total -> repository.getAllTransactions(it.first, it.second)
            }
        }
        .replay()
        .autoConnect()

    override fun fetchForCurrency(currency: Currency) =
        currencySource.onNext(currency)

    override fun fetchForMonth(month: Date) =
        monthSource.onNext(month)

    override fun fetchForCategory(spendingGroup: SpendingGroup) =
        spendingGroupSource.onNext(spendingGroup)

    private fun<T> Flowable<List<T>>.firstOrEmpty(): Flowable<T> {
        return this.flatMap {
            when {
                it.isEmpty() -> Flowable.empty()
                it[0] == null -> Flowable.empty()
                else -> Flowable.just(it[0])
            }
        }
    }
}