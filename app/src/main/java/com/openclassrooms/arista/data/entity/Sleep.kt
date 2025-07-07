package com.openclassrooms.arista.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "sleep",
    foreignKeys = [androidx.room.ForeignKey(
        entity = UserDto::class,
        parentColumns = ["id"],
        childColumns = ["userId"]
    )])
data class SleepDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "startTime")
    var startTime: LocalDateTime,

    @ColumnInfo(name = "duration")
    var duration: Int,

    @ColumnInfo(name = "quality")
    var quality: Int,

    @ColumnInfo(name = "userId")
    val userId: Long = 0
)