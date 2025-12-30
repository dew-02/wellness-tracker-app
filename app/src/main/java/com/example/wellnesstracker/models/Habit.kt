package com.example.wellnesstracker.models

import com.example.wellnesstracker.R

data class Habit(
    var title: String,
    var completedForToday: Boolean = false,
    var iconRes: Int = R.drawable.ic_habit_placeholder, // assign default icon

)
