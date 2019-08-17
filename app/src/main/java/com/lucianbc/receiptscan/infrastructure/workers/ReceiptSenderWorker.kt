package com.lucianbc.receiptscan.infrastructure.workers

import android.content.Context
import androidx.work.*
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.lucianbc.receiptscan.domain.model.ExportedReceipt
import com.lucianbc.receiptscan.domain.model.ImagePath
import com.lucianbc.receiptscan.domain.model.SharingOption
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.ReceiptSender
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import com.lucianbc.receiptscan.util.loge
import javax.inject.Inject

class ReceiptSenderWorker(
    context: Context,
    workParams: WorkerParameters,
    private val repo: DraftsRepository,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val imagesDao: ImagesDao
) : Worker(context, workParams) {

    override fun doWork(): Result {
         val id = inputData.getLong(RECEIPT_ID_KEY, ERROR_ID)
        val appId = inputData.getString(APP_ID_KEY)

        if (id == ERROR_ID) {
            loge("Passed ID was -1")
            return Result.failure()
        }

        if (appId == null) {
            loge("AppId was null")
            return Result.failure()
        }

        return try {
            val result = repo.getExported(id).blockingGet()
            sendText(result, appId)
            sendImage(result)
            Result.success()
        } catch (e: Throwable) {
            loge("Error sending the receipt", e)
            Result.failure()
        }
    }

    private fun sendText(value: ExportedReceipt, appId: String): DocumentReference? {
        val sendTask =
            firestore
                .collection(COLLECTION)
                .add(hashMapOf(
                    "appId" to appId,
                    "receipt" to value
                ))
        return Tasks.await(sendTask)
    }

    private fun sendImage(value: ExportedReceipt): UploadTask.TaskSnapshot? {
        val imageUri = imagesDao.accessFile(ImagePath(value.imagePath))
        val sendTask =
            storage
                .reference
                .child("$COLLECTION/${value.imagePath}")
                .putFile(imageUri)
        return Tasks.await(sendTask)
    }

    class Runner @Inject constructor(
        private val context: Context,
        private val sharingOption: SharingOption
    ) : ReceiptSender {
        override fun send(id: Long) {
            OneTimeWorkRequest
                .Builder(ReceiptSenderWorker::class.java)
                .setConstraints(
                    Constraints
                        .Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setInputData(
                    Data
                        .Builder()
                        .putLong(RECEIPT_ID_KEY, id)
                        .putString(APP_ID_KEY, sharingOption.appId)
                        .build()
                )
                .build()
                .let(WorkManager.getInstance(context)::enqueue)
        }
    }

    class Factory @Inject constructor(
        private val repo: DraftsRepository,
        private val firestore: FirebaseFirestore,
        private val storage: FirebaseStorage,
        private val imagesDao: ImagesDao
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, workParams: WorkerParameters) =
            ReceiptSenderWorker(appContext, workParams, repo, firestore, storage, imagesDao)
    }

    companion object {
        private const val RECEIPT_ID_KEY = "RECEIPT_ID"
        private const val ERROR_ID = -1L

        private const val APP_ID_KEY = "APP_ID"

        private const val COLLECTION = "collected"
    }
}
