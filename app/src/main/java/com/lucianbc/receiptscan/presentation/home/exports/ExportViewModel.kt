package com.lucianbc.receiptscan.presentation.home.exports

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ExportViewModel @Inject constructor(
    exportUseCase: ExportUseCase
) : ViewModel() {
    val exports = exportUseCase.list()
}