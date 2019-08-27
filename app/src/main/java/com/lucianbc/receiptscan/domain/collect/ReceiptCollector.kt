package com.lucianbc.receiptscan.domain.collect

interface ReceiptCollector {
    fun send(id: Long)
}