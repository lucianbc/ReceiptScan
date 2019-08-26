package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.model.ExportedReceipt
import com.lucianbc.receiptscan.domain.repository.AppRepository
import com.lucianbc.receiptscan.domain.usecase.ListReceiptsUseCase
import com.lucianbc.receiptscan.infrastructure.dao.Converters
import com.lucianbc.receiptscan.infrastructure.dao.DraftDao
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val draftDao: DraftDao
) : AppRepository {
    override fun getTextReceiptsBeteewn(firstDate: Date, lastDate: Date) =
        draftDao.getTextReceiptsBetween(
            Converters.toTimestamp(firstDate)!!,
            Converters.toTimestamp(lastDate)!!
        )

    override fun getImageReceiptsBetween(firstDate: Date, lastDate: Date) =
        draftDao.getImageReceiptsBetween(
            Converters.toTimestamp(firstDate)!!,
            Converters.toTimestamp(lastDate)!!
        )

    override fun getAllReceiptItems(): Flowable<List<ListReceiptsUseCase.Item>> =
        draftDao.getReceiptItems()

    override fun getReceipt(id: Long) = draftDao.getReceipt(id)

    override fun getExported(id: Long): Single<ExportedReceipt> = draftDao.getExported(id)
}