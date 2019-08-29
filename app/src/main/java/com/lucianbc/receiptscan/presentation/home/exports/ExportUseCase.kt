package com.lucianbc.receiptscan.presentation.home.exports

import com.lucianbc.receiptscan.domain.export.ExportRepository
import com.lucianbc.receiptscan.domain.export.Status
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ExportUseCase @Inject constructor(
    val repository: ExportRepository
) {
    fun list() = repository.linst()

    fun markAsFinished(id: String, downloadUrl: String) =
        repository
            .updateStatus(id, Status.COMPLETE, downloadUrl)
            .subscribeOn(Schedulers.io())
}