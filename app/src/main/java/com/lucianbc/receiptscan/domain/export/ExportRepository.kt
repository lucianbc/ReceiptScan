package com.lucianbc.receiptscan.domain.export

import com.lucianbc.receiptscan.presentation.home.exports.ExportUseCase
import io.reactivex.Single
import java.util.*

interface ExportRepository {
    fun getTextReceiptsBeteewn(firstDate: Date, lastDate: Date): Single<ExportUseCase.TextReceipt>
    fun getImageReceiptsBetween(firstDate: Date, lastDate: Date): Single<ExportUseCase.ImageReceipt>
}