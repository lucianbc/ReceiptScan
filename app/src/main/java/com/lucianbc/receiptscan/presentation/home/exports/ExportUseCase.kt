package com.lucianbc.receiptscan.presentation.home.exports

import androidx.room.Relation
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.lucianbc.receiptscan.domain.export.Session
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.ImagePath
import com.lucianbc.receiptscan.domain.model.Product
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import io.reactivex.Completable
import java.util.*
import javax.inject.Inject

class ExportUseCase(
    private val repo: DraftsRepository,
    private val imagesDao: ImagesDao,
    private val storage: FirebaseStorage,
    private val manifest: Session
) {
    fun execute(): Completable {
        return sendContent().andThen(sendManifest())
    }

    private fun sendContent(): Completable {
        return when(manifest.content) {
            Session.Content.TextOnly ->
                repo.getTextReceiptsBeteewn(manifest.firstDate, manifest.lastDate)
                    .flatMapCompletable(::send)
            Session.Content.TextAndImage ->
                repo.getImageReceiptsBetween(manifest.firstDate, manifest.lastDate)
                    .flatMapCompletable(::send)
        }
    }

    private fun sendManifest(): Completable =
        Completable.fromCallable {
            val task = storage.reference
                .child("manifests")
                .child("${manifest.id}.json")
                .putBytes(gson.toJson(manifest).toString().toByteArray())
            Tasks.await(task)
        }

    private fun send(value: ImageReceipt): Completable {
        val textTask = Completable.fromCallable {
            val reference = baseReference.child("receipt_${value.id}.json")
            val text = gson.toJson(value)
            val task = reference.putBytes(text.toByteArray())
            Tasks.await(task)
        }

        val imageTask = Completable.fromCallable {
            val reference = baseReference.child(value.imagePath)
            val uri = imagesDao.accessFile(ImagePath(value.imagePath))
            val task = reference.putFile(uri)
            Tasks.await(task)
        }

        return textTask.andThen(imageTask)
    }

    private fun send(value: TextReceipt): Completable {
        return Completable.fromCallable {
            val reference = baseReference.child("receipt_${value.id}.json")
            val text = gson.toJson(value)
            val task = reference.putBytes(text.toByteArray())
            Tasks.await(task)
        }
    }

    private val baseReference = storage.reference.child(manifest.id)

    private val gson = Gson()

    operator fun invoke() = execute()

    data class TextReceipt(
        val id: Long,
        val merchantName: String,
        val date: Date,
        val total: Float,
        val currency: Currency,
        val category: Category,
        @Relation(parentColumn = "id", entityColumn = "receiptId")
        val products: List<Product>
    )

    data class ImageReceipt(
        val id: Long,
        val merchantName: String,
        val date: Date,
        val total: Float,
        val currency: Currency,
        val category: Category,
        val imagePath: String,
        @Relation(parentColumn = "id", entityColumn = "receiptId")
        val products: List<Product>
    )

    class Factory @Inject constructor(
        private val repo: DraftsRepository,
        private val storage: FirebaseStorage,
        private val imagesDao: ImagesDao
    ) {
        fun create(manifest: Session): ExportUseCase {
            return ExportUseCase(repo, imagesDao, storage, manifest)
        }
    }
}