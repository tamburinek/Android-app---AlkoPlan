package com.example.alkoplan.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userName: String,
    val birthDate: String,
    val password: String,
    val photoUrl: String = "",
    var is_LoggedIn: Boolean
)