package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.domain.model.Annotations
import com.lucianbc.receiptscan.domain.model.OcrElements
import com.lucianbc.receiptscan.domain.model.Tag
import com.lucianbc.receiptscan.domain.model.toAnnotation
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
}