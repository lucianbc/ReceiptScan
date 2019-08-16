package com.lucianbc.receiptscan.infrastructure.workers

import android.content.Context
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import com.lucianbc.receiptscan.domain.service.ReceiptSender
import javax.inject.Inject

class ReceiptCollector(
    context: Context,
    workParams: WorkerParameters,
    private val repo: DraftsRepository
) : Worker(context, workParams) {

    override fun doWork(): Result {
        return Result.success()
    }

    class Runner @Inject constructor(
        private val context: Context
    ) : ReceiptSender {
        override fun send(id: Long) {
            WorkManager.getInstance(context)
        }
    }

    class Factory @Inject constructor(
        private val repo: DraftsRepository
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, workParams: WorkerParameters) =
            ReceiptCollector(appContext, workParams, repo)
    }
}
