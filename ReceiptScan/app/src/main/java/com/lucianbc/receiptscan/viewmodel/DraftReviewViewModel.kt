package com.lucianbc.receiptscan.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lucianbc.receiptscan.domain.dao.ioLiveData
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.repository.DraftWithImage
import com.lucianbc.receiptscan.domain.repository.ReceiptDraftRepository
import com.lucianbc.receiptscan.domain.repository.draft
import com.lucianbc.receiptscan.domain.repository.image
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.view.fragment.scanner.widget.GraphicPresenter
import com.lucianbc.receiptscan.view.fragment.scanner.widget.OcrGraphic
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DraftReviewViewModel @Inject constructor(
    private val receiptDraftRepository: ReceiptDraftRepository
): ViewModel()  {
    lateinit var image: LiveData<Bitmap>
    lateinit var data: LiveData<ReceiptDraft>

    lateinit var presenter: GraphicPresenter

    fun initialize(draftId: ID) {
        val draft = receiptDraftRepository.loadDraft(draftId)
        image = draft
            .flatMap { r -> receiptDraftRepository.loadImage(r.imagePath).map { r to it } }
            .observeOn(Schedulers.computation())
            .map { ReceiptGraphicPresenter(it) }
            .doOnNext { presenter = it }
            .map { it.image }
            .ioLiveData()
        data = draft.ioLiveData()
    }

    fun imageTapped(x: Float, y: Float) {
        val graphic = presenter.graphicAtLocation(x, y)
        logd("Tap position: $x, $y")
        logd("Graphic tapped: $graphic")
    }

    private class ReceiptGraphicPresenter(
        draft: DraftWithImage
    ): GraphicPresenter(draft.image.width, draft.image.height) {
        val image: Bitmap
        init {
            graphics.addAll(draft.draft.annotations.map { OcrGraphic(this, it) }.toSet())
            image = draft.image.copy(draft.image.config, true)
            render(Canvas(image))
        }
    }
}
