package com.openclassrooms.arista.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.arista.domain.model.ExerciseCategory
import java.time.LocalDateTime

@Entity(
    tableName = "exercise",
    foreignKeys = [androidx.room.ForeignKey(
        entity = UserDto::class,
        parentColumns = ["id"],
        childColumns = ["userId"]
    )]
)
data class ExerciseDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "debut")
    var startTime: LocalDateTime,

    @ColumnInfo(name = "duree")
    var duration: Int,

    @ColumnInfo(name = "categorie")
    var category: ExerciseCategory = ExerciseCategory.Running,

    @ColumnInfo(name = "intensite")
    var intensity: Int,

    @ColumnInfo(name = "userId")
    val userId: Long = 0
)