package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.TaggingService
import dagger.Module
import dagger.Provides

@Module
class DummiesModule {

    @Provides
    fun provideDummyTaggingService(): TaggingService {
        return object : TaggingService {
            override fun tag(elements: OcrElements): Annotations =
                elements.map { it.toAnnotation(Tag.Noise) }
        }
    }

    @Provides
    fun provideDummyDraftsRepo(): DraftsRepository {
        return object : DraftsRepository {
            override fun create(command: CreateDraftCommand) = 1L
        }
    }

}