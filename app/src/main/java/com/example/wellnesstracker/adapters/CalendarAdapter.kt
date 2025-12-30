package com.example.wellnesstracker.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.wellnesstracker.R

class CalendarAdapter(
    private val context: Context,
    private val days: List<String>,
    private val moods: List<String>
) : BaseAdapter() {

    override fun getCount(): Int = days.size
    override fun getItem(position: Int): Any = days[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_calendar_day, parent, false)

        val txtDay = view.findViewById<TextView>(R.id.txtDayNumber)
        val txtMood = view.findViewById<TextView>(R.id.txtMoodEmoji)

        txtDay.text = days[position]
        txtMood.text = moods[position]

        return view
    }
}
