package com.openclassrooms.arista.ui.exercise

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.arista.R
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.ui.exercise.ExerciseAdapter.ExerciseViewHolder
import java.time.format.DateTimeFormatter

class ExerciseAdapter(private val context: DeleteExerciseInterface) :
    ListAdapter<Exercise, ExerciseViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        holder.tvStartTime.text =
            String.format("Start Time: %s", exercise!!.startTime.format(formatter))
        holder.tvDuration.text = "Duration: ${exercise.duration}"
        holder.tvCategory.text = String.format("Category: %s", exercise.category.toString())
        holder.tvIntensity.text = String.format("Intensity: %d", exercise.intensity)
        holder.ivDelete.setOnClickListener { _: View? -> context.deleteExercise(exercise) }
    }

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvStartTime: TextView
        var tvDuration: TextView
        var tvCategory: TextView
        var tvIntensity: TextView
        var ivDelete: ImageView

        init {
            tvStartTime = itemView.findViewById(R.id.tv_start_time)
            tvDuration = itemView.findViewById(R.id.tv_duration)
            tvCategory = itemView.findViewById(R.id.tv_category)
            tvIntensity = itemView.findViewById(R.id.tv_intensity)
            ivDelete = itemView.findViewById(R.id.delete)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Exercise> =
            object : DiffUtil.ItemCallback<Exercise>() {
                override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
                    return oldItem == newItem
                }
            }
    }
}