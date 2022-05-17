package com.example.alkoplan.data.event

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEvent(event: Event)

    @Query("SELECT * FROM event_data ORDER BY id ASC")
    fun readAllData(): LiveData<List<Event>>

    @Query("SELECT * FROM event_data WHERE userName = :userName")
    fun getAllUserEvents(userName: String): LiveData<List<Event>>

    @Query("SELECT * FROM event_data WHERE userName = :userName")
    fun getLastUserEvent(userName: String): List<Event>

    @Query("SELECT * FROM event_data WHERE title = :title AND userName = :userName")
    fun getEventByTitle(title:String, userName: String) : List<Event>
}