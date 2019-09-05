package com.lucianbc.receiptscan.presentation.home.receipts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.receipts.Group
import com.lucianbc.receiptscan.domain.receipts.ReceiptsUseCase
import com.lucianbc.receiptscan.domain.receipts.SpendingGroup
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

    val categories = mld(dummySpendings())

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

    private fun dummySpendings(): List<SpendingGroup> {
        val ron = Currency.getInstance("RON")

        return listOf(
            SpendingGroup(Group.Total, 600f, ron),
            SpendingGroup(Group.Categorized(Category.Grocery), 140f, ron),
            SpendingGroup(Group.Categorized(Category.Transportation), 140f, ron),
            SpendingGroup(Group.Categorized(Category.Coffee), 140f, ron),
            SpendingGroup(Group.Categorized(Category.Snack), 140f, ron)
        )
    }

    fun fetchForMonth(newMonth: Date) {
        logd("New month fetched $newMonth")
    }

    fun fetchForSpendingGroup(spendingGroup: SpendingGroup) {
        logd("New spending group displayed")
    }
}