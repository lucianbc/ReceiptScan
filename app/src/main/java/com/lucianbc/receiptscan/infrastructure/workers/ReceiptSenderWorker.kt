package com.lucianbc.receiptscan.infrastructure.workers

import android.content.Context
import androidx.work.*
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lucianbc.receiptscan.domain.collect.CollectRepository
import com.lucianbc.receiptscan.domain.collect.CollectingOption
import com.lucianbc.receiptscan.domain.collect.Receipt
import com.lucianbc.receiptscan.domain.collect.ReceiptCollector
import com.lucianbc.receiptscan.domain.model.ImagePath
import com.lucianbc.receiptscan.infrastructure.dao.ImagesDao
import com.lucianbc.receiptscan.util.loge
import com.lucianbc.receiptscan.util.logi
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import javax.inject.Inject

class ReceiptSenderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workParams: WorkerParameters,
    private val repository: CollectRepository,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val imagesDao: ImagesDao
) : Worker(appContext, workParams) {

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
            val result = repository.getReceipt(id).blockingGet()
            sendText(result, appId)
            sendImage(result)
            logi("Receipt with id $id was successfuly sent")
            Result.success()
        } catch (e: Throwable) {
            loge("Error sending the receipt", e)
            Result.failure()
        }
    }

    private fun sendText(value: Receipt, appId: String) =
        firestore
            .collection(COLLECTION)
            .add(
                hashMapOf(
                    "appId" to appId,
                    "receipt" to value
                )
            ).let { Tasks.await(it) }

    private fun sendImage(value: Receipt) =
        imagesDao
            .accessFile(ImagePath(value.imagePath))
            .let {
                storage
                    .reference
                    .child("$COLLECTION/${value.imagePath}")
                    .putFile(it)
            }.let { Tasks.await(it) }

    class Runner @Inject constructor(
        private val context: Context,
        private val collectingOption: CollectingOption
    ) : ReceiptCollector {
        override fun send(id: Long) {
            OneTimeWorkRequest
                .Builder(ReceiptSenderWorker::class.java)
                .setConstraints(
                    Constraints
                        .Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build()
                )
                .setInputData(
                    Data
                        .Builder()
                        .putLong(RECEIPT_ID_KEY, id)
                        .putString(APP_ID_KEY, collectingOption.appId)
                        .build()
                )
                .build()
                .let(WorkManager.getInstance(context)::enqueue)
        }
    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory {
        override fun create(appContext: Context, workParams: WorkerParameters): ReceiptSenderWorker
    }

    companion object {
        private const val RECEIPT_ID_KEY = "RECEIPT_ID"
        private const val ERROR_ID = -1L

        private const val APP_ID_KEY = "APP_ID"

        private const val COLLECTION = "collected"
    }
}
