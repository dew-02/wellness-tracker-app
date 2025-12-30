package com.example.wellnesstracker.workers

import android.content.Context
import android.util.Log //  Added for debugging
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wellnesstracker.utils.NotificationHelper

class HydrationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        //  New: Random motivational message for notifications
        val messages = listOf(
            "Stay hydrated — log a quick sip! 💧",
            "Water time! Keep your energy up. ⚡",
            "Sip sip hooray! Stay fresh today 🧊",
            "Your body loves water — drink now! 🚰",
            "Hydration = Motivation 💦"
        )
        val randomMessage = messages.random()

        //  Show dynamic hydration notification
        NotificationHelper.showNotification(
            context = applicationContext,
            title = "Time to drink water 💧",
            text = randomMessage
        )

        // Added log for testing confirmation
        Log.d("HydrationWorker", "Hydration reminder triggered successfully")

        return Result.success()
    }
}
