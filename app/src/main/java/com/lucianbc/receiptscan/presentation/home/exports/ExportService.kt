package com.lucianbc.receiptscan.presentation.home.exports

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.export.Session
import com.lucianbc.receiptscan.presentation.home.HomePagerAdapter
import com.lucianbc.receiptscan.presentation.home.MainActivity

class ExportService : Service() {

    private lateinit var session : Session

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        session = intent?.extras?.getParcelable<Session>(SESSION_KEY)!!

        startForeground(1, notification())

        return START_NOT_STICKY
    }

    private fun notification(): Notification {
        val tapIntent = MainActivity.navIntent(this, HomePagerAdapter.EXPORT)
        val pendingIntent = PendingIntent.getActivity(this, 0, tapIntent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification MOFO")
            .setContentText("Sending MoFo")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentIntent(pendingIntent)
            .build()
    }

    companion object {
        fun intent(context: Context, session: Session): Intent {
            return Intent(context, ExportService::class.java).apply {
                putExtra(SESSION_KEY, session)
            }
        }

        private const val SESSION_KEY = "SESSION_KEY"
    }
}
