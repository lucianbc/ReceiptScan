package com.lucianbc.receiptscan.infrastructure.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lucianbc.receiptscan.domain.repository.DraftsRepository
import javax.inject.Inject

class ReceiptCollector(
    context: Context,
    workParams: WorkerParameters,
    repo: DraftsRepository
) : Worker(context, workParams) {

    override fun doWork(): Result {
        return Result.success()
    }



    class Factory @Inject constructor(
        private val repo: DraftsRepository
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, workParams: WorkerParameters) =
            ReceiptCollector(appContext, workParams, repo)
    }
}
