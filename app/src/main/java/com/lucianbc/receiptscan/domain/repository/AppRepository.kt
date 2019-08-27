package com.lucianbc.receiptscan.domain.repository

import com.lucianbc.receiptscan.domain.model.ExportedReceipt
import com.lucianbc.receiptscan.presentation.home.exports.ExportUseCase
import io.reactivex.Single
import java.util.*

interface AppRepository {
    fun getExported(id: Long): Single<ExportedReceipt>
    fun getTextReceiptsBeteewn(firstDate: Date, lastDate: Date): Single<ExportUseCase.TextReceipt>
    fun getImageReceiptsBetween(firstDate: Date, lastDate: Date): Single<ExportUseCase.ImageReceipt>
}