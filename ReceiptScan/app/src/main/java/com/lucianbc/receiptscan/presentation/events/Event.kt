package com.lucianbc.receiptscan.presentation.events

import com.otaliastudios.cameraview.PictureResult

sealed class Event {
    data class PictureTaken(val pictureResult: PictureResult)
    data class ImageScanned(val draftId: Long)
}