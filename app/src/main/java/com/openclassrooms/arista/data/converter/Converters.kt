package com.openclassrooms.arista.data.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime()
        }
    }

    @TypeConverter
    fun dateToTimestamp(dateTime: LocalDateTime?): Long? {
        return dateTime?.minusDays(1)
            ?.atZone(ZoneOffset.UTC)
            ?.toInstant()
            ?.toEpochMilli()
    }
}

