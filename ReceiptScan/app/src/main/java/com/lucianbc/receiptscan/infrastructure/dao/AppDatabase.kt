package com.lucianbc.receiptscan.infrastructure.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.domain.model.Draft
import com.lucianbc.receiptscan.domain.model.ProductDraft

@Database(entities = [
    Draft::class,
    Annotation::class,
    ProductDraft::class
], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

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
                    .addMigrations(MIGRATION_1_2)
                    .build()
                }
            }
            return instance!!
        }
    }
}