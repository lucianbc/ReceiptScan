package com.lucianbc.receiptscan.presentation.draft

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.Annotation
import com.lucianbc.receiptscan.domain.model.ReceiptDraftWithProducts
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class DraftReviewViewModel (
    private val draftId: Long,
    private val draftsRepository: DraftsRepository
) : ViewModel() {
    private val _receipt = draftsRepository.getReceipt(draftId)
    private val _image = draftsRepository.getImage(draftId)
    private val _annotations = draftsRepository.getAnnotations(draftId)

    val receipt: LiveData<ReceiptDraftWithProducts>
        get() = _receipt.toLiveData()
    val image: LiveData<Bitmap>
        get() = _image.toLiveData()
    val annotations: LiveData<List<Annotation>>
        get() = _annotations.toLiveData()


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
}