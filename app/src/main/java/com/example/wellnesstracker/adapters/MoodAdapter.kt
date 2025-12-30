package com.example.wellnesstracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.R
import com.example.wellnesstracker.databinding.ItemMoodBinding
import com.example.wellnesstracker.models.MoodEntry
import java.text.SimpleDateFormat
import java.util.*

class MoodAdapter(private val moods: MutableList<MoodEntry>) :
    RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    inner class MoodViewHolder(val binding: ItemMoodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val binding = ItemMoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moods[position]
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        holder.binding.tvEmoji.text = mood.emoji
        holder.binding.tvMoodLabel.text = mood.moodLabel
        holder.binding.tvDate.text = sdf.format(Date(mood.timestamp))
        holder.binding.tvNote.text = mood.note ?: ""

        // 🌈 Optional: change card color based on emoji
        val context = holder.binding.root.context
        val bgColor = when (mood.emoji) {
            "😄", "😊", "😁" -> R.color.teal_700      // Happy moods
            "🙂", "😌" -> R.color.purple_200         // Calm moods
            "😐", "😕" -> R.color.teal_700           // Neutral moods
            "😔", "😢" -> R.color.purple_200         // Sad moods
            "😡", "😤" -> R.color.purple_200              // Angry moods
            else -> R.color.white
        }
        holder.binding.root.setCardBackgroundColor(ContextCompat.getColor(context, bgColor))
    }

    override fun getItemCount(): Int = moods.size
}
