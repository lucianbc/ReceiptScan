package com.lucianbc.receiptscan.domain.receipts

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.withLatestFrom
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

    val availableMonths = currentCurrency
        .flatMap {
            repository
                .getAvailableMonths(it)
        }
        .map { it.sortedDescending() }
        .replay(1)
        .autoConnect()

    private val queryMonth = monthSource
        .mergeWith(availableMonths.firstOrEmpty())

    override val categories = currentCurrency
        .withLatestFrom(queryMonth)
        .flatMap {
            repository.getAllSpendings(it.first, it.second)
        }
        .replay(1)
        .autoConnect()

    override val currentSpending = spendingGroupSource
        .mergeWith(categories.firstOrEmpty())

    override val transactions = currentCurrency
        .withLatestFrom(queryMonth, currentSpending)
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
            if (it.isEmpty())
                Flowable.empty()
            else
                Flowable.just(it[0])
        }
    }
}