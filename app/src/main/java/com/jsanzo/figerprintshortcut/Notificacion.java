package com.jsanzo.figerprintshortcut;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.ContextWrapper;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class Notificacion extends ContextWrapper {
    private NotificationManager notifManager;
    public static final String CHANNEL_ONE_ID = "ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";

    private boolean onGoing;

    public Notificacion(Context base, boolean onGoing) {
        super(base);
        this.onGoing = onGoing;
        createChannels();
    }

    private void createChannels() {

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, notifManager.IMPORTANCE_LOW);
        notificationChannel.enableLights(false);
        notificationChannel.setShowBadge(false);
        notificationChannel.setVibrationPattern(null);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        getManager().createNotificationChannel(notificationChannel);
    }

    public Notification.Builder getNotification1(String title, String body) {
        Intent intent = new Intent(Notificacion.this, MainActivityOld.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Notificacion.this, 0,intent, PendingIntent.FLAG_IMMUTABLE);

        return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setColor(0xFFFF7931)
                .setOngoing(onGoing)
                .setSmallIcon(R.drawable.ic_action_name)
                .setAutoCancel(false);
    }

    public void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

    public NotificationManager getManager() {
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notifManager;
    }
}
