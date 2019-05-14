package com.lucianbc.receiptscan.viewmodel

sealed class Event {
    object ImportImage: Event()
    object ImageScanned: Event()
}
