package com.example.inventory_application;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    public static final String CHANNEL_ID = "inventory_alerts";

    public static void ensureChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Inventory Alerts",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationChannel.setDescription("Alerts when items quantity reaches 0");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            if(notificationManager != null) notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    public static void notifyWhenQuantityReachesZero(Context context, String itemName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Out of Stock")
                .setContentText(itemName + "'s quantity has reached 0")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context).notify(itemName.hashCode(), builder.build());
    }
}
