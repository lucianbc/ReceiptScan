package com.lucianbc.receiptscan.presentation.draft

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.toLiveData
import com.lucianbc.receiptscan.domain.model.DraftWithProducts
import com.lucianbc.receiptscan.domain.model.OcrElement
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.presentation.service.paint
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.rxkotlin.combineLatest
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

class DraftReviewViewModel (
    private val draftId: Long,
    private val draftsRepository: DraftsRepository
) : ViewModel() {

    private val _receipt = draftsRepository.getReceipt(draftId)
    private val _ocrElements: Flowable<List<OcrElement>> = draftsRepository.getOcrElements(draftId)
    private val _image: Flowable<Bitmap> = draftsRepository.getImage(draftId)

    private val _updateTotalSource = PublishSubject.create<Float>()
    private val _updateMerchantSource = PublishSubject.create<String>()
    private val _updateDateSource = PublishSubject.create<Date>()

    private val _receiptObservable = _receipt.toObservable()

    private val _totalPipe = _updateTotalSource
        .withLatestFrom(_receiptObservable)
        .map {
            it.second.receipt.copy(total = it.first)
        }

    private val _merchantPipe = _updateMerchantSource
        .withLatestFrom(_receiptObservable)
        .map {
            it.second.receipt.copy(merchantName = it.first)
        }

    private val _datePipe = _updateDateSource
        .withLatestFrom(_receiptObservable)
        .map {
            it.second.receipt.copy(date = it.first)
        }

    private val _addProductSource = PublishSubject.create<Product>()
    private val _removeProductSource = PublishSubject.create<Product>()
    private val _replaceProductSource = PublishSubject.create<Product>()

    private val _addProductPipe = _addProductSource
        .withLatestFrom(_receiptObservable)
        .map {
            it.second.products + it.first
        }

    private val _removeProductPipe = _removeProductSource
        .withLatestFrom(_receiptObservable)
        .map { it.second.products - it.first }

    private val _updateProductPipe = _replaceProductSource
        .withLatestFrom(_receiptObservable)
        .map { it.second.products.map { p -> if (p.id == it.first.id) it.first else p } }

    init {
        Observable.merge(_totalPipe, _merchantPipe, _datePipe)
            .flatMapSingle { draftsRepository.update(it).subscribeOn(Schedulers.io()) }
            .subscribe()

        val d = Observable.merge(_addProductPipe, _removeProductPipe, _updateProductPipe)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMapSingle {
                draftsRepository.update(draftId, it)
            }
            .subscribe({
                println("Finished")
            }, {
                println("Error")
            })
    }

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

    fun validateDraft() {
        Observable
            .fromCallable { draftsRepository.validate(draftId) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateTotal(newVal: Float) = _updateTotalSource.onNext(newVal)

    fun updateMerchant(newVal: String) = _updateMerchantSource.onNext(newVal)

    fun updateDate(newDate: Date) = _updateDateSource.onNext(newDate)

    fun updateProducts(newProduct: Product) = _replaceProductSource.onNext(newProduct)

    fun addProduct(name: String, price: Float) = _addProductSource.onNext(Product(name, price, receiptId = draftId))

    fun deleteProduct(product: Product) = _removeProductSource.onNext(product)

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
                throw IllegalArgumentException("ViewModel not fond or receipt id not instantiated")
            }
        }
    }
    // endregion
}
