package com.lucianbc.receiptscan.infrastructure.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lucianbc.receiptscan.infrastructure.dao.PreferencesDao
import com.lucianbc.receiptscan.util.logd
import dagger.android.AndroidInjection
import javax.inject.Inject

class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var preferencesDao: PreferencesDao

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        logd("Received notification from firebase")
        logd("Payload was ${message.data}")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        logd("Generated new token")
        logd("Here it is: $token")
        preferencesDao.setNotificationToken(token)
    }
}
