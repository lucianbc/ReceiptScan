package com.lucianbc.receiptscan.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass


@Module
internal abstract class ViewModelFactoryBinds {
    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerAwareViewModelFactory):
            ViewModelProvider.Factory
}

class DaggerAwareViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("Unknown model class $modelClass")
        }
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
