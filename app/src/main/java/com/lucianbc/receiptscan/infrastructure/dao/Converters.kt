package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.TypeConverter
import com.lucianbc.receiptscan.util.toDate
import java.util.*

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromCurrency(currency: Currency?) = currency?.currencyCode

        @TypeConverter
        @JvmStatic
        fun toCurrency(currencyCode: String?): Currency? =
            if (currencyCode == null) null else Currency.getInstance(currencyCode)

        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: Long?) = value?.toDate()

        @TypeConverter
        @JvmStatic
        fun toTimestamp(value: Date?) = value?.time
    }
}