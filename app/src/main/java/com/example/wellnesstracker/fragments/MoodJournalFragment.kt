package com.example.wellnesstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellnesstracker.R
import com.example.wellnesstracker.adapters.MoodAdapter
import com.example.wellnesstracker.databinding.FragmentMoodJournalBinding
import com.example.wellnesstracker.models.MoodEntry
import com.example.wellnesstracker.utils.PrefsHelper
import java.text.SimpleDateFormat
import java.util.*

class MoodJournalFragment : Fragment() {

    private var _binding: FragmentMoodJournalBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PrefsHelper
    private val moods = mutableListOf<MoodEntry>()
    private lateinit var adapter: MoodAdapter

    private val emojiList = listOf("😄", "🙂", "😐", "😔", "😢", "😡")

    // Readable labels for emojis
    private val emojiMap = mapOf(
        "😄" to "Happy",
        "🙂" to "Good",
        "😐" to "Neutral",
        "😔" to "Sad",
        "😢" to "Crying",
        "😡" to "Angry"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoodJournalBinding.inflate(inflater, container, false)
        prefs = PrefsHelper(requireContext())

        // Load previously saved moods
        moods.addAll(prefs.loadMoods())

        // Set up RecyclerView
        adapter = MoodAdapter(moods)
        binding.recyclerMoods.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMoods.adapter = adapter

        // Show today’s date
        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        binding.moodDate.text = "Today, ${dateFormat.format(Date())}"

        // Create emoji buttons dynamically
        emojiList.forEach { emoji ->
            val btn = layoutInflater.inflate(android.R.layout.simple_list_item_1, null) as TextView
            btn.text = emoji
            btn.textSize = 24f
            btn.setPadding(20, 10, 20, 10)
            btn.setOnClickListener {
                showAddNoteDialog(emoji)
            }
            binding.emojiContainer.addView(btn)
        }

        // When pressing main "Log Current Mood" button
        binding.btnShareSummary.setOnClickListener {
            showAddNoteDialog("😐")
        }

        // Floating Add Button
        binding.fabAddMood.setOnClickListener {
            showAddNoteDialog("😐")
        }

        // Calendar icon click: navigate to CalendarFragment
       // binding.btnCalendar.setOnClickListener {
            //findNavController().navigate(R.id.action_moodJournalFragment_to_calendarFragment)
        //}

        return binding.root
    }

    private fun showAddNoteDialog(chosenEmoji: String) {
        val et = EditText(requireContext())
        et.hint = "Add an optional note"
        AlertDialog.Builder(requireContext())
            .setTitle("Add Mood $chosenEmoji")
            .setView(et)
            .setPositiveButton("Save") { _, _ ->
                val note = et.text.toString().takeIf { it.isNotBlank() }

                val mood = MoodEntry(
                    emoji = chosenEmoji,
                    moodLabel = emojiMap[chosenEmoji] ?: "Unknown",
                    note = note
                )

                moods.add(0, mood)
                prefs.saveMoods(moods)
                adapter.notifyItemInserted(0)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
