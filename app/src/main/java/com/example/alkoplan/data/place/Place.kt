package com.example.alkoplan.data.place

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place_data")
class Place (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description:String,
    val placeName:String,
    val rating: Float,
    val photoPlace : String,
    val menuItems: Int,
    val address: String
)