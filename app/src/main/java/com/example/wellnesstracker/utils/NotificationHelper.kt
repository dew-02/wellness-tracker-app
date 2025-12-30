package com.example.wellnesstracker.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager //  Added for sound
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.wellnesstracker.R

object NotificationHelper {
    private const val CHANNEL_ID = "hydration_channel"
    private const val CHANNEL_NAME = "Hydration Reminders"

    //  Create notification channel (only once for Android 8.0+)
    fun createChannel(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH //  Increased importance for visibility
            ).apply {
                description = "Reminders to drink water regularly"
                enableVibration(true)
            }
            nm.createNotificationChannel(channel)
        }
    }

    //  Display a hydration notification
    fun showNotification(context: Context, title: String, text: String) {
        createChannel(context)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_water) //  Make sure this icon exists
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(notificationSound) // Added sound
            .setVibrate(longArrayOf(0, 300, 200, 300)) //  Added vibration
            .build()

        //  Use a unique ID so each reminder shows separately
        nm.notify((System.currentTimeMillis() % 10000).toInt(), notification)
    }
}
