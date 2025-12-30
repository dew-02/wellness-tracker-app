package com.example.wellnesstracker.utils

import android.content.Context
import com.example.wellnesstracker.models.Habit
import com.example.wellnesstracker.models.MoodEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefsHelper(private val context: Context) {

    private val prefs = context.getSharedPreferences("wellnesstracker_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // ------------------ Habits ------------------
    fun saveHabits(habits: List<Habit>) {
        prefs.edit().putString("habits_json", gson.toJson(habits)).apply()
    }

    fun loadHabits(): MutableList<Habit> {
        val json = prefs.getString("habits_json", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Habit>>() {}.type
        return gson.fromJson(json, type)
    }

    // ------------------ Mood entries (for RecyclerView) ------------------
    fun saveMoods(moods: List<MoodEntry>) {
        prefs.edit().putString("moods_json", gson.toJson(moods)).apply()
    }

    fun loadMoods(): MutableList<MoodEntry> {
        val json = prefs.getString("moods_json", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<MoodEntry>>() {}.type
        return gson.fromJson(json, type)
    }

    // ------------------ Mood data by date (for CalendarFragment) ------------------
    // Save single mood for a specific date (yyyy-MM-dd)
    fun saveMood(date: String, emoji: String) {
        prefs.edit().putString(date, emoji).apply()
    }

    // Get all moods as Map<date, emoji>
    fun getMoodData(): Map<String, String> {
        val allPrefs = prefs.all
        val moodMap = mutableMapOf<String, String>()
        for ((key, value) in allPrefs) {
            // Only include string values that are not JSON arrays (avoid habits/moods list JSON)
            if (value is String && !key.endsWith("_json")) {
                moodMap[key] = value
            }
        }
        return moodMap
    }

    // ------------------ Settings ------------------
    fun setHydrationIntervalMinutes(minutes: Int) {
        prefs.edit().putInt("hydration_interval", minutes).apply()
    }

    fun getHydrationIntervalMinutes(): Int = prefs.getInt("hydration_interval", 120)
}
