package com.lucianbc.receiptscan.domain.dao

import androidx.room.TypeConverter
import com.lucianbc.receiptscan.domain.model.Tag

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromTag(tag: Tag) = tag.ordinal

        @TypeConverter
        @JvmStatic
        fun toTag(value: Int) = Tag.values().firstOrNull { it.ordinal == value } ?: Tag.Noise
    }
}