package com.lucianbc.receiptscan.presentation.home.receipts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.receipts.ReceiptsUseCase
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.util.mld
import java.util.*
import javax.inject.Inject

class ReceiptsViewModel @Inject constructor(
    receiptsUseCase: ReceiptsUseCase
) : ViewModel() {
    val receipts = receiptsUseCase.list().toLiveData()

    val currencies =
        mld(
            listOf("RON", "EUR", "GBP", "USD")
                .map { Currency.getInstance(it) }
        )

    val months = mld(dummyMonths())

    fun fetchForCurrency(newCurrency: Currency) {
        logd("New currency fetched: ${newCurrency.currencyCode}")
    }

    private fun dummyMonths(): List<Date> {
        val now = Date()
        val calendar = Calendar.getInstance().apply { time = now }
        return (0 until 24).map {
            calendar.add(Calendar.MONTH, -1)
            calendar.time
        }
    }

    fun fetchForMonth(newMonth: Date) {
        logd("New month fetched $newMonth")
    }
}