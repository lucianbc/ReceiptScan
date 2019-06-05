package com.lucianbc.receiptscan.domain.service

import com.lucianbc.receiptscan.domain.model.Annotations
import com.lucianbc.receiptscan.domain.model.OcrElements

interface TaggingService {
    fun tag(elements: OcrElements): Annotations
}