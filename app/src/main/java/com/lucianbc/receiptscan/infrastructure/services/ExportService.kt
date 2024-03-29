package com.lucianbc.receiptscan.infrastructure.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.lucianbc.receiptscan.R
import com.lucianbc.receiptscan.domain.export.ExportUseCase
import com.lucianbc.receiptscan.domain.export.Session
import com.lucianbc.receiptscan.presentation.home.HomePagerAdapter
import com.lucianbc.receiptscan.presentation.home.MainActivity
import com.lucianbc.receiptscan.presentation.home.exports.CHANNEL_ID
import dagger.android.DaggerService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ExportService : DaggerService() {

    private lateinit var session : Session

    @Inject
    lateinit var useCase: ExportUseCase

    private val disposables = CompositeDisposable()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        session = intent?.extras?.getParcelable(SESSION_KEY)!!

        startForeground(1, notification())

        useCase.newExport(session)
            .subscribeOn(Schedulers.io())
            .subscribe {
                stopSelf()
            }
            .addTo(disposables)

        return START_NOT_STICKY
    }

    private fun notification(): Notification {
        val tapIntent = MainActivity.navIntent(this, HomePagerAdapter.EXPORT)
        val pendingIntent = PendingIntent.getActivity(this, 0, tapIntent, 0)

        return NotificationCompat.Builder(this,
            CHANNEL_ID
        )
            .setContentTitle("Exporting")
            .setContentText("Uploading your data to cloud")
            .setProgress(0, 0, true)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
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
