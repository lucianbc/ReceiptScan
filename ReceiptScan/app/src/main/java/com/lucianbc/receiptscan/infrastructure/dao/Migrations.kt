package com.lucianbc.receiptscan.infrastructure.dao

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `productDraft` (`name` TEXT NOT NULL, `price` REAL NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT, `draftId` INTEGER, FOREIGN KEY(`draftId`) REFERENCES `draft`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        database.execSQL("CREATE  INDEX `index_productDraft_draftId` ON `productDraft` (`draftId`)")
    }
}