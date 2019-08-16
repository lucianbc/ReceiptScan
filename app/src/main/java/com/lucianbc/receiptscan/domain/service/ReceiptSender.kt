package com.lucianbc.receiptscan.domain.service

interface ReceiptSender {
    fun send(id: Long)
}