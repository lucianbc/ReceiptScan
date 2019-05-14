package com.lucianbc.receiptscan.viewmodel

import android.graphics.Bitmap
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.util.Just
import com.lucianbc.receiptscan.util.None
import com.lucianbc.receiptscan.util.Optional
import javax.inject.Inject
import javax.inject.Singleton

typealias DraftWithImage = Pair<ReceiptDraft, Bitmap>

val DraftWithImage.image
    get() = this.second

val DraftWithImage.draft
    get() = this.first

@Singleton
class ReceiptDraftCache @Inject constructor() {
    private var value: Optional<DraftWithImage> = None

    fun provide(draftWithImage: DraftWithImage) {
        value = Just(draftWithImage)
    }

    fun consume(): Optional<DraftWithImage> {
        val result =  value
        value = None
        return result
    }
}
