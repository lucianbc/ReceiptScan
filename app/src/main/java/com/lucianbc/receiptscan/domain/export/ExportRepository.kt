package com.lucianbc.receiptscan.domain.export

import com.lucianbc.receiptscan.presentation.home.exports.UploadUseCase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

interface ExportRepository {
    fun linst(): Flowable<List<Export>>
    fun getTextReceiptsBeteewn(firstDate: Date, lastDate: Date): Single<List<UploadUseCase.TextReceipt>>
    fun getImageReceiptsBetween(firstDate: Date, lastDate: Date): Single<List<UploadUseCase.ImageReceipt>>
    fun persist(session: Session, status: Status): Completable
    fun updateStatus(id: String, status: Status): Completable
    fun updateStatus(id: String, status: Status, downloadLink: String): Completable
}