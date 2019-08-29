package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.export.Export
import com.lucianbc.receiptscan.domain.export.ExportRepository
import com.lucianbc.receiptscan.domain.export.Session
import com.lucianbc.receiptscan.domain.export.Status
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import com.lucianbc.receiptscan.infrastructure.dao.Converters
import com.lucianbc.receiptscan.infrastructure.entities.ExportEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.*
import javax.inject.Inject

class ExportRepositoryImpl @Inject constructor(
    private val appDao: AppDao
) : ExportRepository {

    override fun persist(session: Session, status: Status) =
        session.run {
            ExportEntity(
                id,
                firstDate,
                lastDate,
                content,
                format,
                status,
                "",
                Date()
            )
        }.let(appDao::insert)

    override fun linst(): Flowable<List<Export>> {
        return appDao.selectExports()
    }

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


    override fun updateStatus(id: String, status: Status) = appDao.updateStatus(id, status)

    override fun updateStatus(id: String, status: Status, downloadLink: String) = appDao.updateStatusAndLink(id, status, downloadLink)
}