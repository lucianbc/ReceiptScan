package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.model.ExportedReceipt
import com.lucianbc.receiptscan.domain.repository.AppRepository
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import com.lucianbc.receiptscan.infrastructure.dao.Converters
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : AppRepository {
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

    override fun getReceipt(id: Long) = appDao.getReceipt(id)

    override fun getExported(id: Long): Single<ExportedReceipt> = appDao.getExported(id)
}