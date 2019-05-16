package com.lucianbc.receiptscan.domain.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanInfoBox

@Database(entities = [ReceiptDraft::class, ScanInfoBox::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun receiptDraftDao(): ReceiptDraftDao

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
                        .build()
                }
            }
            return instance!!
        }
    }
}