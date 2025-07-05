package com.openclassrooms.arista.domain.model

import com.openclassrooms.arista.data.entity.SleepDto
import java.time.LocalDateTime

data class Sleep(
    @JvmField
    var id: Long,
    var startTime: LocalDateTime,
    var duration: Int,
    var quality: Int,
    var userId: Long
) {
    companion object {
        fun fromDto(dto: SleepDto): Sleep {
            return Sleep(
                id = dto.id,
                startTime = dto.startTime,
                duration = dto.duration,
                quality = dto.quality,
                userId = dto.userId
            )
        }
    }

    fun toDto(): SleepDto {
        return SleepDto(
            id = this.id,
            startTime = this.startTime,
            duration = this.duration,
            quality = this.quality,
            userId = this.userId
        )
    }
}
