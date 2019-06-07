package com.lucianbc.receiptscan.di

import com.lucianbc.receiptscan.domain.model.*
import com.lucianbc.receiptscan.domain.service.ProductsAndTotalStrategy
import com.lucianbc.receiptscan.domain.service.TaggingService
import com.lucianbc.receiptscan.domain.service.extractDate
import com.lucianbc.receiptscan.domain.service.extractMerchant
import dagger.Module
import dagger.Provides

@Module
class DummiesModule {

    @Provides
    fun provideDummyTaggingService(): TaggingService {
        return SomeTaggingService()
    }
}


class SomeTaggingService: TaggingService {
    override fun tag(elements: OcrElements): Annotations  {
        val rawReceipt = RawReceipt.create(elements.toList())

        val merchant = extractMerchant(rawReceipt)
        val date = extractDate(rawReceipt.receiptText)

        val (price, products) = ProductsAndTotalStrategy(rawReceipt).execute(1L)

        return elements.map { it.toAnnotation(Tag.Noise) }
    }
}