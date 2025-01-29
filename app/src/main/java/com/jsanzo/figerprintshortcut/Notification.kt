package com.jsanzo.figerprintshortcut

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent

class Notificacion(base: Context?, private val onGoing: Boolean) : ContextWrapper(base) {

    private var manager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ONE_ID: String = "ONE"
        const val CHANNEL_ONE_NAME: String = "Service status reminder"
    }

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
        manager.createNotificationChannel(notificationChannel)
    }

    fun getNotification(title: String?, body: String?): Notification.Builder {
        val intent = Intent(this@Notificacion, MainActivity::class.java)
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

    fun showNotification(id: Int, notification: Notification.Builder) {
        manager.notify(id, notification.build())
    }

    fun hideNotification(id: Int) {
        manager.cancel(id)
    }
}