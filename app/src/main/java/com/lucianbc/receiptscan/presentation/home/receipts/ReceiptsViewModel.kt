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

    val availableCurrencies = mld(dummyCurrencies())

    val availableMonths = mld(dummyMonths())

    val categories = mld(dummySpendings())

    val selectedCategory = mld(dummySpendings()[0])

    fun fetchForCurrency(newCurrency: Currency) {
        logd("New currency fetched: ${newCurrency.currencyCode}")
    }

    fun fetchForMonth(newMonth: Date) {
        logd("New month fetched $newMonth")
    }

    fun fetchForSpendingGroup(spendingGroup: SpendingGroup) {
        logd("New spending group displayed")
        selectedCategory.postValue(spendingGroup)
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

    private fun dummyCurrencies(): List<Currency> =
        listOf("RON", "EUR", "GBP", "USD")
            .map { Currency.getInstance(it) }
}