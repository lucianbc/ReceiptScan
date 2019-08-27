package com.lucianbc.receiptscan.domain.repository

import com.lucianbc.receiptscan.domain.model.ExportedReceipt
import com.lucianbc.receiptscan.domain.usecase.ManageReceiptUseCase
import com.lucianbc.receiptscan.presentation.home.exports.ExportUseCase
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

interface AppRepository {
    fun getReceipt(id: Long): Flowable<ManageReceiptUseCase.Value>
    fun getExported(id: Long): Single<ExportedReceipt>
    fun getTextReceiptsBeteewn(firstDate: Date, lastDate: Date): Single<ExportUseCase.TextReceipt>
    fun getImageReceiptsBetween(firstDate: Date, lastDate: Date): Single<ExportUseCase.ImageReceipt>
}