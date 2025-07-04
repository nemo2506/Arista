package com.openclassrooms.arista.domain.model

import com.openclassrooms.arista.domain.model.ExerciseCategory
import com.openclassrooms.arista.data.entity.ExerciseDto
import java.time.LocalDateTime

data class Exercise(
    val id: Long? = null,
    var startTime: LocalDateTime,
    var duration: Int,
    var category: ExerciseCategory,
    var intensity: Int
) {
    companion object {
        fun fromDto(dto: ExerciseDto): Exercise {
            return Exercise(
                id = dto.id,
                startTime = dto.startTime,
                duration = dto.duration,
                category = dto.category,
                intensity = dto.intensity
            )
        }
    }

    fun toDto(): ExerciseDto {
        return ExerciseDto(
            id = this.id ?: 0, // 0 means auto-generated in Room
            startTime = this.startTime,
            duration = this.duration,
            category = this.category,
            intensity = this.intensity
        )
    }
}