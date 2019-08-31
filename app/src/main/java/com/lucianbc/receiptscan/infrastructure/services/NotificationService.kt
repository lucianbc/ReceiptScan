package com.lucianbc.receiptscan.infrastructure.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.export.FinishedNotification
import com.lucianbc.receiptscan.infrastructure.dao.PreferencesDao
import com.lucianbc.receiptscan.presentation.home.HomePagerAdapter
import com.lucianbc.receiptscan.presentation.home.MainActivity
import com.lucianbc.receiptscan.presentation.home.exports.CHANNEL_ID
import com.lucianbc.receiptscan.presentation.home.exports.ExportUseCaseImpl
import com.lucianbc.receiptscan.util.logd
import com.lucianbc.receiptscan.util.loge
import com.lucianbc.receiptscan.util.logi
import dagger.android.AndroidInjection
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var preferencesDao: PreferencesDao

    @Inject
    lateinit var exportUseCase: ExportUseCaseImpl

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        logi("Received message!")
        showNotification()
        handleMessage(message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        logd("Generated new token")
        logd("Here it is: $token")
        preferencesDao.setNotificationToken(token)
    }

    @SuppressLint("CheckResult")
    private fun handleMessage(message: RemoteMessage) {

        message
            .parseNotification()
            .observeOn(Schedulers.io())
            .flatMapCompletable(exportUseCase::markAsFinished)
            .andThen(showNotification())
            .doOnComplete {
                logi("Message successfuly handled")
            }
            .doOnError {
                loge("Error handling message", it)
            }
            .subscribe()
    }

    private fun RemoteMessage.parseNotification(): Single<FinishedNotification> {
        val exportId = Single.fromCallable {
            data["requestId"] ?: throw IllegalArgumentException("RequestId was null")
        }
        val downloadUrl = Single.fromCallable {
            data["url"] ?: throw IllegalArgumentException("Download Url was null")
        }

        return exportId
            .zipWith(downloadUrl)
            .map { (id: String, url: String) ->
                FinishedNotification(id, url)
            }
    }

    private fun showNotification(): Completable {
        val tapIntent = MainActivity.navIntent(this, HomePagerAdapter.EXPORT)
        val pendingIntent = PendingIntent.getActivity(this, 0, tapIntent, 0)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Export finished")
            .setContentText("Your files are ready to be downloaded")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentIntent(pendingIntent)
            .build()
        return Completable.fromCallable {
            with(NotificationManagerCompat.from(this)) {
                notify(1, notification)
            }
        }
    }
}
