package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.export.ExportRepository
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import com.lucianbc.receiptscan.infrastructure.dao.Converters
import java.util.*
import javax.inject.Inject

class ExportRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : ExportRepository {
    override fun getTextReceiptsBeteewn(firstDate: Date, lastDate: Date) =
        appDao.getTextReceiptsBetween(
            Converters.toTimestamp(firstDate)!!,
            Converters.toTimestamp(lastDate)!!
        )

    override fun getImageReceiptsBetween(firstDate: Date, lastDate: Date) =
        appDao.getImageReceiptsBetween(
            Converters.toTimestamp(firstDate)!!,
            Converters.toTimestamp(lastDate)!!
        )
}