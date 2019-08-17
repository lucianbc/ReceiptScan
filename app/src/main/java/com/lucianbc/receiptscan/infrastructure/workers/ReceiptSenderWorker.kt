package com.lucianbc.receiptscan.infrastructure.workers

import android.content.Context
import androidx.work.*
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.lucianbc.receiptscan.domain.model.SharingOption
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.ReceiptSender
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.util.loge
import com.lucianbc.receiptscan.util.takeSingle
import javax.inject.Inject

class ReceiptSenderWorker(
    context: Context,
    workParams: WorkerParameters,
    private val repo: DraftsRepository,
    private val firestore: FirebaseFirestore
) : Worker(context, workParams) {

    override fun doWork(): Result {
        logd("Doing work in a fokin worker.")

        val id = inputData.getLong(RECEIPT_ID_KEY, ERROR_ID)
        val appId = inputData.getString(APP_ID_KEY)

        if (id == ERROR_ID) {
            loge("Passed ID was -1")
            return Result.failure()
        }

        return try {
            val result = repo.getReceipt(id).takeSingle().blockingGet()

            val sendTask =
                firestore
                    .collection(COLLECTION)
                    .add(hashMapOf(
                        "appId" to appId,
                        "receipt" to result
                    ))

            Tasks.await(sendTask)

            Result.success()
        } catch (e: Throwable) {
            loge("Error sending the receipt", e)
            Result.failure()
        }
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
        private val firestore: FirebaseFirestore
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, workParams: WorkerParameters) =
            ReceiptSenderWorker(appContext, workParams, repo, firestore)
    }

    companion object {
        private const val RECEIPT_ID_KEY = "RECEIPT_ID"
        private const val ERROR_ID = -1L

        private const val APP_ID_KEY = "APP_ID"

        private const val COLLECTION = "collected"
    }
}
