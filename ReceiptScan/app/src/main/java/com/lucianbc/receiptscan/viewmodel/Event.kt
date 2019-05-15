package com.lucianbc.receiptscan.viewmodel

import com.lucianbc.receiptscan.domain.model.ID

sealed class Event {
    object ImportImage: Event()
    class  ImageScanned(val draftId: ID): Event()
}
