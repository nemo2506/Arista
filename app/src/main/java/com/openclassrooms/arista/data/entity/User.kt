package com.openclassrooms.arista.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,


    @ColumnInfo(name = "nom")
    var name: String,


    @ColumnInfo(name = "email")
    var email: String,


    @ColumnInfo(name = "motDePasse")
    var password: String
)