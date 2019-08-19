package com.lucianbc.receiptscan.presentation

import com.lucianbc.receiptscan.domain.export.Session
import com.lucianbc.receiptscan.domain.model.Category
import com.otaliastudios.cameraview.PictureResult
import java.util.*

sealed class Event {
    data class PictureTaken(val pictureResult: PictureResult) : Event()
    data class ImageScanned(val draftId: Long) : Event()
    class CurrencyTapped(val callback: (Currency) -> Unit): Event()
    class CategoryTapped(val callback: (Category) -> Unit): Event()
    class ExportForm(val callback: (Session) -> Unit): Event()
}