package com.example.alkoplan.data.event

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class EventDatabase: RoomDatabase() {

    abstract fun eventDao(): EventDao

    // singleton instance of db
    companion object{
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase(context: Context): EventDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_data"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}