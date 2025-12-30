package com.example.wellnesstracker.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellnesstracker.adapters.HabitsAdapter
import com.example.wellnesstracker.databinding.FragmentHabitsBinding
import com.example.wellnesstracker.models.Habit
import com.example.wellnesstracker.utils.PrefsHelper
import java.text.SimpleDateFormat
import java.util.*

class HabitsFragment : Fragment() {

    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: PrefsHelper
    private val habits = mutableListOf<Habit>()
    private lateinit var adapter: HabitsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        prefs = PrefsHelper(requireContext())

        // Load saved habits
        habits.addAll(prefs.loadHabits())

        // Set date
        setCurrentDate()

        // RecyclerView
        adapter = HabitsAdapter(
            habits,
            onToggle = { idx, checked ->
                habits[idx].completedForToday = checked
                prefs.saveHabits(habits)
                updateProgress()
            },
            onEdit = { idx -> showEditDialog(idx) },
            onDelete = { idx ->
                habits.removeAt(idx)
                prefs.saveHabits(habits)
                adapter.notifyItemRemoved(idx)
                updateProgress()
            }
        )

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        // FAB
        binding.fabAddHabit.setOnClickListener { showAddDialog() }

        // Combined progress
        updateProgress()

        return binding.root
    }

    private fun setCurrentDate() {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(Date())
    }

    private fun showAddDialog() {
        val input = EditText(requireContext())
        input.hint = "Enter habit title"
        AlertDialog.Builder(requireContext())
            .setTitle("Add Habit")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val title = input.text.toString().trim()
                if (title.isNotEmpty()) {
                    val habit = Habit(title)
                    habits.add(habit)
                    prefs.saveHabits(habits)
                    adapter.notifyItemInserted(habits.size - 1)
                    updateProgress()
                    binding.recycler.scrollToPosition(habits.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog(idx: Int) {
        val et = EditText(requireContext())
        et.setText(habits[idx].title)
        AlertDialog.Builder(requireContext())
            .setTitle("Edit Habit")
            .setView(et)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = et.text.toString().trim()
                if (newTitle.isNotEmpty()) {
                    habits[idx].title = newTitle
                    prefs.saveHabits(habits)
                    adapter.notifyItemChanged(idx)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateProgress() {
        // Combined progress
        val total = habits.size
        val done = habits.count { it.completedForToday }
        val percent = if (total == 0) 0 else (done * 100 / total)
        // Update combined progress
        binding.tvProgress.text = "Today's Progress: $done / $total ($percent%)"
        binding.progressBar.progress = percent
        binding.tvProgressPercent.text = "$percent%"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
