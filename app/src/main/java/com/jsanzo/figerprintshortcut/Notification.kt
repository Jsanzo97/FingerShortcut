package com.jsanzo.figerprintshortcut

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent

class Notificacion(base: Context?, private val onGoing: Boolean) : ContextWrapper(base) {
    private var notifManager: NotificationManager? = null

    init {
        createChannels()
    }

    private fun createChannels() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ONE_ID,
            CHANNEL_ONE_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.enableLights(false)
        notificationChannel.setShowBadge(false)
        notificationChannel.vibrationPattern = null
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
        manager!!.createNotificationChannel(notificationChannel)
    }

    fun getNotification1(title: String?, body: String?): Notification.Builder {
        val intent = Intent(
            this@Notificacion,
            MainActivity::class.java
        )
        val pendingIntent =
            PendingIntent.getActivity(this@Notificacion, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return Notification.Builder(applicationContext, CHANNEL_ONE_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setColor(-0x86cf)
            .setOngoing(onGoing)
            .setSmallIcon(R.drawable.ic_action_name)
            .setAutoCancel(false)
    }

    fun notify(id: Int, notification: Notification.Builder) {
        manager!!.notify(id, notification.build())
    }

    val manager: NotificationManager?
        get() {
            if (notifManager == null) {
                notifManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return notifManager
        }

    companion object {
        const val CHANNEL_ONE_ID: String = "ONE"
        const val CHANNEL_ONE_NAME: String = "Channel One"
    }
}