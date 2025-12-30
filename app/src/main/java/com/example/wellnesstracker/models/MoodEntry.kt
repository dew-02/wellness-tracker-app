package com.example.wellnesstracker.models

data class MoodEntry(
    val id: Long = System.currentTimeMillis(),
    val timestamp: Long = System.currentTimeMillis(),
    val emoji: String,
    val moodLabel: String,
    val note: String? = null
)