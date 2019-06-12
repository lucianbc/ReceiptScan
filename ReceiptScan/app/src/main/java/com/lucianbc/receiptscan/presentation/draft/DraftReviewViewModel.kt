package com.lucianbc.receiptscan.presentation.draft

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.OcrElement
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.presentation.service.paint
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.rxkotlin.combineLatest
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DraftReviewViewModel (
    private val draftId: Long,
    private val draftsRepository: DraftsRepository
) : ViewModel() {

    private val _receipt = draftsRepository
        .getReceipt(draftId)
    private val _ocrElements: Flowable<List<OcrElement>> = draftsRepository.getOcrElements(draftId)
    private val _image: Flowable<Bitmap> = draftsRepository.getImage(draftId)


    private val _drawnImage = _image
        .combineLatest(_ocrElements)
        .map { paint(it.first, it.second) }

    val receipt: LiveData<DraftWithProducts>
        get() = _receipt.toLiveData()
    val image: LiveData<Bitmap>
        get() = _drawnImage.toLiveData()

    fun discardDraft() {
        Observable
            .fromCallable { draftsRepository.delete(draftId) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }


    // region Factory
    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val draftsRepository: DraftsRepository
    ) : ViewModelProvider.Factory {
        var draftId = -1L

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(DraftReviewViewModel::class.java) && draftId != -1L) {
                DraftReviewViewModel(draftId, draftsRepository) as T
            } else {
                throw IllegalArgumentException("ViewModel not fond or draft id not instantiated")
            }
        }
    }
    // endregion
}
