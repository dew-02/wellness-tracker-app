package com.example.wellnesstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wellnesstracker.databinding.ActivityMainBinding
import com.example.wellnesstracker.fragments.ChartFragment
import com.example.wellnesstracker.fragments.HabitsFragment
import com.example.wellnesstracker.fragments.MoodJournalFragment
import com.example.wellnesstracker.fragments.CalendarFragment
import com.example.wellnesstracker.fragments.SettingsFragment
import com.example.wellnesstracker.workers.HydrationWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Default fragment on launch
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, HabitsFragment())
            .commit()

        //  Set up bottom navigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            val frag = when (item.itemId) {
                R.id.nav_habits -> HabitsFragment()
                R.id.nav_mood -> MoodJournalFragment()
                R.id.nav_calendar -> CalendarFragment()
                R.id.nav_chart -> ChartFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> HabitsFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, frag)
                .commit()
            true
        }

        //  For testing during exam: trigger notification instantly
        testInstantHydrationNotification()

        //  Schedule background hydration reminder (every 15 minutes for testing)
        scheduleHydrationReminder()
    }

    // Function to test instant notification
    private fun testInstantHydrationNotification() {
        val testWork = OneTimeWorkRequestBuilder<HydrationWorker>().build()
        WorkManager.getInstance(this).enqueue(testWork)
    }

    //  Function to schedule periodic reminders every 15 minutes
    private fun scheduleHydrationReminder() {
        val periodicWork = PeriodicWorkRequestBuilder<HydrationWorker>(
            15, TimeUnit.MINUTES // 🔁 triggers every 15 mins for testing
        ).build()

        // Ensures only one active reminder worker runs
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "hydration_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWork
        )
    }
}
