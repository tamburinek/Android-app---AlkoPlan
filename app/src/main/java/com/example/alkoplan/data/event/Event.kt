package com.example.alkoplan.data.event

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_data")
class Event (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val descriptionEvent: String,
    val userName: String,
    val place: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
)