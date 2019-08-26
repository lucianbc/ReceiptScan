package com.lucianbc.receiptscan.domain.drafts

import javax.inject.Inject

class DraftsUseCaseImpl @Inject constructor(
    private val repository: DraftsRepository
) : DraftsUseCase {

    override fun list() = repository.listDrafts()
}