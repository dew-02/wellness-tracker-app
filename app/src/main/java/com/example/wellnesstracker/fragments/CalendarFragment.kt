package com.example.wellnesstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.wellnesstracker.R
import com.example.wellnesstracker.utils.PrefsHelper
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var gridCalendar: GridView
    private lateinit var txtMonthYear: TextView
    private lateinit var prefs: PrefsHelper

    private val daysList = mutableListOf<String>()
    private val moodsList = mutableListOf<String>()
    private val emojiList = listOf("😄", "🙂", "😐", "😔", "😢", "😡")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        gridCalendar = view.findViewById(R.id.gridCalendar)
        txtMonthYear = view.findViewById(R.id.txtMonthYear)
        prefs = PrefsHelper(requireContext())

        setupCalendar()

        gridCalendar.setOnItemClickListener { _, _, position, _ ->
            val day = daysList[position]
            if (day.isNotEmpty()) {
                val calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                val month = sdf.format(Date())
                val dateKey = "$month-${day.padStart(2, '0')}" // e.g., 2025-10-08
                showAddMoodDialog(dateKey, position)
            }
        }

        return view
    }

    private fun setupCalendar() {
        val calendar = Calendar.getInstance()
        val sdfMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        txtMonthYear.text = sdfMonth.format(calendar.time)

        // First day of month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        // Days in month
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Load moods
        val savedMoods = prefs.getMoodData()

        daysList.clear()
        moodsList.clear()

        // Add empty cells for offset
        for (i in 0 until firstDayOfWeek) {
            daysList.add("")
            moodsList.add("")
        }

        // Fill days and moods
        val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tempCal = Calendar.getInstance()
        tempCal.set(Calendar.DAY_OF_MONTH, 1)
        for (day in 1..daysInMonth) {
            tempCal.set(Calendar.DAY_OF_MONTH, day)
            val dateKey = sdfKey.format(tempCal.time)
            daysList.add(day.toString())
            moodsList.add(savedMoods[dateKey] ?: "")
        }

        gridCalendar.adapter = CalendarAdapter(requireContext(), daysList, moodsList)
    }

    private fun showAddMoodDialog(dateKey: String, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Mood for $dateKey")
        val moodButtons = emojiList.toTypedArray()

        builder.setItems(moodButtons) { _, which ->
            val selectedEmoji = moodButtons[which]
            prefs.saveMood(dateKey, selectedEmoji)
            moodsList[position] = selectedEmoji
            (gridCalendar.adapter as CalendarAdapter).notifyDataSetChanged()
            Toast.makeText(requireContext(), "Mood saved!", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
