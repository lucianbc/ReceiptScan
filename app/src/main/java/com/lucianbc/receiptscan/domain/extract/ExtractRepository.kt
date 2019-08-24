package com.lucianbc.receiptscan.domain.extract

import android.graphics.Bitmap
import io.reactivex.Single

interface ExtractRepository {
    fun persist(draft: Draft, image: Bitmap): Single<DraftId>
}