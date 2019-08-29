package com.lucianbc.receiptscan.presentation.home.exports

import com.lucianbc.receiptscan.domain.export.ExportRepository
import javax.inject.Inject

class ExportUseCase @Inject constructor(
   val repository: ExportRepository
) {
    fun list() = repository.linst()
}