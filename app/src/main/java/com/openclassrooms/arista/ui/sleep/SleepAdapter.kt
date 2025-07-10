package com.openclassrooms.arista.ui.sleep

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.arista.R
import com.openclassrooms.arista.domain.model.Sleep
import java.time.format.DateTimeFormatter

class SleepAdapter : ListAdapter<Sleep, SleepAdapter.SleepViewHolder>(SleepDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sleep, parent, false)
        return SleepViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
        val sleep = getItem(position)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        holder.tvStartTime.text = "Start Time: ${sleep.startTime.format(formatter)}"
        holder.tvDuration.text = "Duration: ${sleep.duration} minutes"
        holder.tvQuality.text = "Quality: ${sleep.quality}"
    }

    inner class SleepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStartTime: TextView = itemView.findViewById(R.id.tv_start_time)
        val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
        val tvQuality: TextView = itemView.findViewById(R.id.tv_quality)
    }
}

class SleepDiffCallback : DiffUtil.ItemCallback<Sleep>() {
    override fun areItemsTheSame(oldItem: Sleep, newItem: Sleep): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Sleep, newItem: Sleep): Boolean {
        return oldItem == newItem
    }
}
