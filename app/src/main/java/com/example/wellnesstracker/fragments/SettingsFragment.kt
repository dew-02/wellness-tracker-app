package com.example.wellnesstracker.fragments

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.wellnesstracker.R
import com.example.wellnesstracker.workers.HydrationWorker
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private lateinit var enableSwitch: Switch
    private lateinit var intervalText: TextView
    private lateinit var timeText: TextView
    private lateinit var saveButton: Button

    private var selectedInterval = 60
    private var startHour = 8
    private var startMinute = 0

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // find views safely
        enableSwitch = view.findViewById(R.id.switchEnable)
        intervalText = view.findViewById(R.id.textInterval)
        timeText = view.findViewById(R.id.textStartTime)
        saveButton = view.findViewById(R.id.saveButton)

        val prefs = requireContext().getSharedPreferences("wellness_prefs", Context.MODE_PRIVATE)

        // load saved settings
        selectedInterval = prefs.getInt("hydration_interval", 60)
        startHour = prefs.getInt("hydration_start_hour", 8)
        startMinute = prefs.getInt("hydration_start_min", 0)
        enableSwitch.isChecked = prefs.getBoolean("hydration_enabled", false)

        updateIntervalLabel()
        updateTimeLabel()

        // click listeners
        intervalText.setOnClickListener { showIntervalDialog() }
        timeText.setOnClickListener { showTimePicker() }

        saveButton.setOnClickListener {
            val enabled = enableSwitch.isChecked
            prefs.edit()
                .putInt("hydration_interval", selectedInterval)
                .putInt("hydration_start_hour", startHour)
                .putInt("hydration_start_min", startMinute)
                .putBoolean("hydration_enabled", enabled)
                .apply()

            if (enabled) {
                scheduleHydrationReminder(selectedInterval)
                Toast.makeText(requireContext(), "Reminders ON every $selectedInterval min", Toast.LENGTH_SHORT).show()
            } else {
                WorkManager.getInstance(requireContext()).cancelUniqueWork("hydration_work")
                Toast.makeText(requireContext(), "Reminders OFF", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun showIntervalDialog() {
        val options = arrayOf("15 min", "30 min", "1 hour", "2 hours", "4 hours")
        val values = arrayOf(15, 30, 60, 120, 240)

        AlertDialog.Builder(requireContext())
            .setTitle("Select Reminder Interval")
            .setItems(options) { _, which ->
                selectedInterval = values[which]
                updateIntervalLabel()
            }.show()
    }

    private fun showTimePicker() {
        val picker = TimePickerDialog(requireContext(), { _, hour, minute ->
            startHour = hour
            startMinute = minute
            updateTimeLabel()
        }, startHour, startMinute, true)
        picker.show()
    }

    private fun updateIntervalLabel() {
        intervalText.text = when (selectedInterval) {
            15 -> "Every 15 min"
            30 -> "Every 30 min"
            60 -> "Every 1 hour"
            120 -> "Every 2 hours"
            240 -> "Every 4 hours"
            else -> "Every $selectedInterval min"
        }
    }

    private fun updateTimeLabel() {
        timeText.text = "Start Time: %02d:%02d".format(startHour, startMinute)
    }

    private fun scheduleHydrationReminder(minutes: Int) {
        val request = PeriodicWorkRequestBuilder<HydrationWorker>(minutes.toLong(), TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "hydration_work",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }
}
