package com.bubble.probubblebopling.ror.presentation.notificiation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.google.firebase.messaging.RemoteMessage
import com.bubble.probubblebopling.R

private const val CHICKEN_CHANNEL_ID = "probubblebopling_notifications"
private const val CHICKEN_CHANNEL_NAME = "Pro Bubble BoPling Notifications"
private const val CHICKEN_NOT_TAG = "Pro Bubble BoPling"

class ProBubbleBoPlingPushService : com.google.firebase.messaging.FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Обработка notification payload
        remoteMessage.notification?.let {
            if (remoteMessage.data.contains("url")) {
                chickenShowNotification(it.title ?: CHICKEN_NOT_TAG, it.body ?: "", data = remoteMessage.data["url"])
            } else {
                chickenShowNotification(it.title ?: CHICKEN_NOT_TAG, it.body ?: "", data = null)
            }
        }

        // Обработка data payload
        if (remoteMessage.data.isNotEmpty()) {
            chickenHandleDataPayload(remoteMessage.data)
        }
    }

    private fun chickenShowNotification(title: String, message: String, data: String?) {
        val chickenNotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал уведомлений для Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHICKEN_CHANNEL_ID,
                CHICKEN_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            chickenNotificationManager.createNotificationChannel(channel)
        }

        val chickenIntent = Intent(this, _root_ide_package_.com.bubble.probubblebopling.ProBubbleBoPlingActivity::class.java).apply {
            putExtras(bundleOf(
                "url" to data
            ))
            Intent.getIntent(data).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chickenPendingIntent = PendingIntent.getActivity(
            this,
            0,
            chickenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val chickenNotification = NotificationCompat.Builder(this, CHICKEN_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.chicken_noti)
            .setAutoCancel(true)
            .setContentIntent(chickenPendingIntent)
            .build()

        chickenNotificationManager.notify(System.currentTimeMillis().toInt(), chickenNotification)
    }

    private fun chickenHandleDataPayload(data: Map<String, String>) {
        data.forEach { (key, value) ->
            Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Data key=$key value=$value")
        }
    }
}