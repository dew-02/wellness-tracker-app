package com.example.wellnesstracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.R
import com.example.wellnesstracker.databinding.ItemHabitBinding
import com.example.wellnesstracker.models.Habit

class HabitsAdapter(
    private val habits: MutableList<Habit>,
    private val onToggle: (Int, Boolean) -> Unit,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(val binding: ItemHabitBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]

        holder.binding.cbCompleted.text = habit.title
        holder.binding.cbCompleted.isChecked = habit.completedForToday
        holder.binding.pbHabitProgress.progress = if (habit.completedForToday) 100 else 0

        // Example: choose icon based on habit
        val iconRes = when (habit.title.lowercase()) {
            "drink water" -> R.drawable.ic_water_glass
            "meditation" -> R.drawable.ic_meditation
            "exercise" -> R.drawable.ic_exercise
            else -> R.drawable.ic_habit_placeholder
        }
        holder.binding.ivHabitIcon.setImageResource(iconRes)

        holder.binding.cbCompleted.setOnCheckedChangeListener(null)
        holder.binding.cbCompleted.setOnCheckedChangeListener { _: CompoundButton, isChecked ->
            onToggle(position, isChecked)
            holder.binding.pbHabitProgress.progress = if (isChecked) 100 else 0
        }

        holder.binding.btnEdit.setOnClickListener { onEdit(position) }
        holder.binding.btnDelete.setOnClickListener { onDelete(position) }
    }

    override fun getItemCount(): Int = habits.size
}
