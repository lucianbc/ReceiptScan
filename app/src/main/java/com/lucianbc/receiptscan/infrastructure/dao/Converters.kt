package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.TypeConverter
import com.lucianbc.receiptscan.domain.export.Session
import com.lucianbc.receiptscan.domain.export.Status
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.util.toDate
import java.lang.IllegalStateException
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
        fun fromTimestamp(value: Long?): Date? {
            return value?.times(1000)?.toDate()
        }

        @TypeConverter
        @JvmStatic
        fun toTimestamp(value: Date?) = value?.time?.div(1000)

        @TypeConverter
        @JvmStatic
        fun fromCategory(value: String?) =
            Category.values().firstOrNull { c -> c.name == value } ?: Category.Grocery

        @TypeConverter
        @JvmStatic
        fun toCategory(category: Category) = category.name

        @TypeConverter
        @JvmStatic
        fun toStatus(value: String?): Status = value?.let { Status.valueOf(it) }
            ?: throw IllegalStateException("Export status was null")

        @TypeConverter
        @JvmStatic
        fun fromStatus(status: Status) = status.name

        @TypeConverter
        @JvmStatic
        fun toContent(value: String?) = value?.let { Session.Content.valueOf(it) }
            ?: throw IllegalStateException("Export session content was null")

        @TypeConverter
        @JvmStatic
        fun fromContent(content: Session.Content) = content.name

        @TypeConverter
        @JvmStatic
        fun toFormat(value: String?) = value?.let { Session.Format.valueOf(it) }
            ?: throw IllegalStateException("Export session content was null")

        @TypeConverter
        @JvmStatic
        fun fromFormat(format: Session.Format) = format.name
    }
}