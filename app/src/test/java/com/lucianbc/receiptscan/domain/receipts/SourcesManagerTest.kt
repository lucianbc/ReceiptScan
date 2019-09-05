package com.lucianbc.receiptscan.domain.receipts

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class SourcesManagerTest {

    @Test
    fun `when instantiated, currencies are queried`() {
        val repo: ReceiptsRepository = mock {
            on { getAvailableCurrencies() } doReturn Flowable.just(currencies).subscribeOn(Schedulers.io())
        }
        val subject = SourcesManager(repo)
        subject.availableCurrencies.test().awaitCount(1).assertValue(currencies)
        verify(repo, times(1)).getAvailableCurrencies()
    }

    @Test
    fun `when fetching new currency, values are emmited`() {
        val repo: ReceiptsRepository = mock {
            on { getAvailableCurrencies() } doReturn Flowable.just(currencies).subscribeOn(Schedulers.io())
        }
        val subject = SourcesManager(repo)
        subject.availableCurrencies.test()
        val currencyObs = subject.currentCurrency.test()

        subject.fetchForCurrency(otherCurrency)

        currencyObs.awaitCount(2).assertValues(currencies[0], otherCurrency)

        subject.fetchForCurrency(otherCurrency)

        currencyObs.awaitCount(1).assertValues(currencies[0], otherCurrency, otherCurrency)
    }

    @Test
    fun `when subscribing to months, months are fetched for currency`() {
        val repo: ReceiptsRepository = mock {
            on { getAvailableCurrencies() } doReturn Flowable.just(currencies).subscribeOn(Schedulers.io())
            on { getAvailableMonths(otherCurrency) } doReturn Flowable.just(usdMonths).subscribeOn(Schedulers.io())
            on { getAvailableMonths(currencies[1]) } doReturn Flowable.just(eurMonths).subscribeOn(Schedulers.io())
            on { getAvailableMonths(currencies[0]) } doReturn Flowable.just(ronMonths).subscribeOn(Schedulers.io())
        }

        val subject = SourcesManager(repo)

        subject.availableMonths.test().awaitCount(1).assertValue(ronMonths)

        subject.fetchForCurrency(otherCurrency)

        subject.availableMonths.test().awaitCount(2).assertValues(ronMonths, usdMonths)

        verify(repo, times(1)).getAvailableMonths(currencies[0])
        verify(repo, times(1)).getAvailableMonths(otherCurrency)
    }

    private val df = SimpleDateFormat("dd-MM-yyyy", Locale.US)

    private val usdMonths = listOf(df.parse("23-10-2019"), df.parse("23-11-2019")).sortedDescending()
    private val eurMonths = listOf(df.parse("13-02-2018"), df.parse("05-04-2018")).sortedDescending()
    private val ronMonths = listOf(df.parse("13-06-2018"), df.parse("05-07-2018")).sortedDescending()

    private val otherCurrency = Currency.getInstance("USD")

    private val currencies = listOf(
        Currency.getInstance("RON"),
        Currency.getInstance("EUR")
    )
}