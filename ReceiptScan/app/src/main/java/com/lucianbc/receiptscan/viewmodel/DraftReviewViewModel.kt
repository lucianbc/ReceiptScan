package com.lucianbc.receiptscan.viewmodel

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.ID
import com.lucianbc.receiptscan.domain.model.ReceiptDraft
import com.lucianbc.receiptscan.domain.model.ScanInfoBox
import com.lucianbc.receiptscan.domain.model.Tag
import com.lucianbc.receiptscan.domain.repository.DraftWithImage
import com.lucianbc.receiptscan.domain.repository.ReceiptDraftRepository
import com.lucianbc.receiptscan.domain.repository.draft
import com.lucianbc.receiptscan.domain.repository.image
import com.lucianbc.receiptscan.view.fragment.scanner.widget.boundingBoxF
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DraftReviewViewModel @Inject constructor(
    private val receiptDraftRepository: ReceiptDraftRepository
): ViewModel()  {
    lateinit var draft: LiveData<ReceiptDraft>
    lateinit var singleImageDraft: LiveData<DraftWithImage>

    fun initialize(draftId: ID) {
        val data = receiptDraftRepository
            .loadDraft(draftId)

        singleImageDraft = data
            .flatMap { d ->
                receiptDraftRepository
                    .loadImage(d.imagePath)
                    .observeOn(Schedulers.computation())
                    .map { d to it }
                    .map { drawImage(it) }
            }
            .toLiveData()

        draft = data.toLiveData()
    }

    fun changeInfoBox(box: ScanInfoBox) {
        receiptDraftRepository.saveAnnotation(box)
    }

    private fun drawImage(receipt: DraftWithImage): DraftWithImage {
        val showImg = receipt.image.copy(receipt.image.config, true)
        val canvas = Canvas(showImg)
        receipt.draft.annotations.map{ it to it.boundingBoxF }.forEach {
            val radius = 0.3f * it.second.height()
            canvas.drawRoundRect(it.second,
                radius,
                radius,
                if(it.first.tag == Tag.Noise) BOX_PAINT_NOISE else BOX_PAINT_DATA
            )
        }
        return receipt.draft to showImg
    }

    companion object {
        val BOX_PAINT_NOISE = Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
            alpha = 150
        }

        val BOX_PAINT_DATA = Paint().apply {
            style = Paint.Style.FILL
            color = Color.YELLOW
            alpha = 150
        }
    }
}
