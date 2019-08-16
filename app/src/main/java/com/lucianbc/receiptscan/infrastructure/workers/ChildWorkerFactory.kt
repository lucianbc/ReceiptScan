package com.lucianbc.receiptscan.infrastructure.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

interface ChildWorkerFactory {
    fun create(appContext: Context, workParams: WorkerParameters): Worker
}