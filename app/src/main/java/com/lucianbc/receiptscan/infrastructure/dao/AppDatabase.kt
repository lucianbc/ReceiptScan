package com.lucianbc.receiptscan.infrastructure.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lucianbc.receiptscan.domain.model.OcrElement
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.model.ReceiptEntity

@Database(entities = [
    ReceiptEntity::class,
    OcrElement::class,
    Product::class
], version = 4, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun draftDao(): DraftDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "receiptscan.db"
                    )
                        .fallbackToDestructiveMigration()
                    .build()
                }
            }
            return instance!!
        }
    }
}