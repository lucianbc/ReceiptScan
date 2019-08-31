package com.lucianbc.receiptscan.domain.export

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

interface ExportRepository {
    fun linst(): Flowable<List<Export>>
    fun getTextReceiptsBeteewn(firstDate: Date, lastDate: Date): Single<List<TextReceipt>>
    fun getImageReceiptsBetween(firstDate: Date, lastDate: Date): Single<List<ImageReceipt>>
    fun persist(session: Session, status: Status): Completable
    fun updateStatus(id: String, status: Status): Completable
    fun updateStatus(id: String, status: Status, downloadLink: String): Completable
}