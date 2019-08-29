package com.lucianbc.receiptscan.presentation.home.exports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import javax.inject.Inject

class ExportViewModel @Inject constructor(
    exportUseCase: ExportUseCase
) : ViewModel() {
    val exports = exportUseCase.list().toLiveData()
}