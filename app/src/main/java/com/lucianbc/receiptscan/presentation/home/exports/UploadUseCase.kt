package com.lucianbc.receiptscan.presentation.home.exports

import androidx.room.Relation
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.lucianbc.receiptscan.domain.export.ExportRepository
import com.lucianbc.receiptscan.domain.export.Session
import com.lucianbc.receiptscan.domain.export.Status
import com.lucianbc.receiptscan.domain.model.Category
import com.lucianbc.receiptscan.domain.model.ImagePath
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import com.lucianbc.receiptscan.infrastructure.dao.PreferencesDao
import com.lucianbc.receiptscan.infrastructure.entities.ProductEntity
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Completable
import java.util.*

class UploadUseCase @AssistedInject constructor(
    private val repo: ExportRepository,
    private val imagesDao: ImagesDao,
    private val prefs: PreferencesDao,
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    @Assisted private val manifest: Session
) {
    private fun execute(): Completable {
        return repo
            .persist(manifest, Status.UPLOADING)
            .andThen(sendContent())
            .andThen(sendManifest())
            .andThen(repo.updateStatus(manifest.id, Status.WAITING_DOWNLOAD))
    }

    private fun sendContent(): Completable {
        return when(manifest.content) {
            Session.Content.TextOnly ->
                repo.getTextReceiptsBeteewn(manifest.firstDate, manifest.lastDate)
                    .flatMapCompletable { Completable.concat(it.map(this::send)) }
            Session.Content.TextAndImage ->
                repo.getImageReceiptsBetween(manifest.firstDate, manifest.lastDate)
                    .flatMapCompletable { Completable.concat(it.map(this::send)) }
        }
    }

    private fun sendManifest(): Completable =
        Completable.fromCallable {
            val task = firestore
                .collection("manifests")
                .add(Manifest(manifest, prefs.notificationToken))
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
        val productEntities: List<ProductEntity>
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
        val productEntities: List<ProductEntity>
    )

    data class Manifest (
        val firstDate: Date,
        val lastDate: Date,
        val content: Session.Content,
        val format: Session.Format,
        val id: String,
        val notificationToken: String
    ) {
        companion object {
            operator fun invoke(session: Session, notificationToken: String): Manifest {
                return session.run {
                    Manifest (
                        firstDate,
                        lastDate,
                        content,
                        format,
                        id,
                        notificationToken
                    )
                }
            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(manifest: Session): UploadUseCase
    }
}