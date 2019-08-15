package com.lucianbc.receiptscan.infrastructure.dao

import android.content.SharedPreferences
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.ReceiptDefaults
import java.util.*

class PreferencesDao (
    private val prefs: SharedPreferences
) : ReceiptDefaults {

    private var receiptDefaults: ReceiptDefaultsValue

    private var _sendReceipt: Boolean

    val sendReceipt
        get() = _sendReceipt

    override val currency: Currency
        get() = receiptDefaults.currency
    override val category: Category
        get() = receiptDefaults.category

    init {
        receiptDefaults = ReceiptDefaultsValue(
            readCurrency(),
            readCategory()
        )
        _sendReceipt = readSendReceipt()
    }

    fun setCategory(category: Category) = category.write()

    fun setCurrency(currency: Currency) = currency.write()

    fun setSendReceipt(enabled: Boolean) = enabled.write()

    private fun readSendReceipt() =
        prefs.getBoolean(SEND_RECEIPT_KEY, DEFAULT_SEND_RECEIPT)

    private fun Boolean.write() {
        _sendReceipt = this
        prefs.edit().putBoolean(SEND_RECEIPT_KEY, this).apply()
    }

    private fun readCategory() =
        prefs.getString(CATEGORY_KEY, null).let {
            when (it) {
                null -> DEFAULT_CATEGORY
                else -> Category.valueOf(it)
            }
        }

    private fun Category.write() {
        receiptDefaults = receiptDefaults.copy(category = this)
        prefs.edit().putString(CATEGORY_KEY, this.name).apply()
    }

    private fun readCurrency() =
        prefs.getString(CURRENCY_KEY, null).let {
            when (it) {
                null -> DEFAULT_CURRENCY
                else -> Currency.getInstance(it)
            }
        }

    private fun Currency.write() {
        receiptDefaults = receiptDefaults.copy(currency = this)
        prefs.edit().putString(CURRENCY_KEY, this.currencyCode).apply()
    }

    private data class ReceiptDefaultsValue(
        override val currency: Currency,
        override val category: Category
    ) : ReceiptDefaults

    companion object {
        private const val CATEGORY_KEY = "CATEGORY"
        private const val CURRENCY_KEY = "CURRENCY"
        private const val SEND_RECEIPT_KEY = "SEND_RECEIPT"

        private val DEFAULT_CATEGORY = Category.Grocery
        private val DEFAULT_CURRENCY = Currency.getInstance("RON")!!
        private const val DEFAULT_SEND_RECEIPT = false
    }
}