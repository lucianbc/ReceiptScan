package com.lucianbc.receiptscan.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.lucianbc.receiptscan.infrastructure.workers.ChildWorkerFactory
import com.lucianbc.receiptscan.infrastructure.workers.ReceiptCollector
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
internal abstract class WorkerBinds {
    @Binds
    @IntoMap
    @WorkerKey(ReceiptCollector.Factory::class)
    abstract fun bindReceiptCollector(worker: ReceiptCollector.Factory): ChildWorkerFactory

    @Binds
    abstract fun bindFactory(factory: DaggerAwareWorkerFactory): WorkerFactory
}

class DaggerAwareWorkerFactory @Inject constructor(
    private val creators: Map<Class<out ChildWorkerFactory>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerClass = Class.forName(workerClassName)
        var creator : Provider<out ChildWorkerFactory>? = creators[workerClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (workerClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("Unknown worker class $workerClass")
        }
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get().create(appContext, workerParameters)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out ChildWorkerFactory>)
