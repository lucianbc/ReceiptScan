package com.lucianbc.receiptscan.infrastructure.repository

import com.lucianbc.receiptscan.domain.drafts.Draft
import com.lucianbc.receiptscan.domain.drafts.DraftsRepository
import com.lucianbc.receiptscan.domain.drafts.Product
import com.lucianbc.receiptscan.domain.extract.DraftId
import com.lucianbc.receiptscan.domain.model.OcrElementEntity
import com.lucianbc.receiptscan.domain.model.ProductEntity
import com.lucianbc.receiptscan.domain.model.ReceiptEntity
import com.lucianbc.receiptscan.infrastructure.dao.AppDao
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.Function3
import javax.inject.Inject

class DraftsRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val imagesDao: ImagesDao
) : DraftsRepository {
    override fun listDrafts() =
        appDao.listDrafts()

    override fun getDraft(draftId: DraftId) =
        appDao.run {
            Flowable.zip(
                selectDraft(draftId),
                selectProducts(draftId),
                selectOcrElements(draftId),
                Function3(::create)
            )
        }

    override fun getImage(draftId: DraftId) =
        appDao
            .getImagePath(draftId)
            .map(imagesDao::readImage)


    override fun update(draft: Draft): Completable {
        return draft.run {
            appDao.updateDraft(
                merchantName,
                date,
                total,
                currency,
                category,
                id
            )
        }
    }

    override fun delete(draftId: DraftId) =
        appDao.delete(draftId)

    override fun moveDraftToValid(draftId: DraftId) =
        appDao.validate(draftId)

    override fun addEmptyProductTo(draftId: DraftId) =
        ProductEntity("", 0f, receiptId = draftId).run {
            appDao
                .insert(this)
                .map { id = it; product() }
        }

    override fun updateProductIn(product: Product, draftId: DraftId): Completable =
        product.run {
            ProductEntity(name, price, id, draftId)
        }.let(appDao::insert).ignoreElement()

    override fun deleteProduct(product: Product) =
        appDao.deleteProduct(product.id)
}

private fun create(
    receiptEntity: ReceiptEntity,
    products: List<ProductEntity>,
    els: List<OcrElementEntity>
): Draft {
    return receiptEntity.run {
        Draft(
            id!!,
            merchantName,
            date!!,
            total,
            currency!!,
            category,
            products.map(ProductEntity::product),
            els.map {
                com.lucianbc.receiptscan.domain.drafts.OcrElement(
                    it.text,
                    it.top,
                    it.bottom,
                    it.left,
                    it.right,
                    it.id!!
                )
            }
        )
    }
}

private fun ProductEntity.product() = Product(
    this.name,
    this.price,
    this.id!!
)
