package com.example.alkoplan.data.place

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlace(place: Place)

    @Query("SELECT * FROM place_data ORDER BY id ASC")
    fun readAllData(): LiveData<List<Place>>

    @Query("SELECT * FROM place_data WHERE id = :id")
    fun getPlaceById(id: Int): Place

    @Query("SELECT * FROM place_data WHERE placeName = :name")
    fun getPlaceByName(name: String): List<Place>
}