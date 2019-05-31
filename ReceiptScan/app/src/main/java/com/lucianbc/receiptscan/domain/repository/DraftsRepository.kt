package com.lucianbc.receiptscan.domain.repository

import com.lucianbc.receiptscan.domain.model.CreateDraftCommand

interface DraftsRepository {
    fun create(command: CreateDraftCommand): Long
}