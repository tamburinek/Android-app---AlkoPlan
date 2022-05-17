package com.example.alkoplan

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room
import com.example.alkoplan.data.event.EventDatabase
import com.example.alkoplan.data.place.PlaceDatabase
import com.example.alkoplan.data.user.UserDatabase
import kotlinx.android.synthetic.main.activity_home.*

class EventDetail : AppCompatActivity() {

    lateinit var eventDb: EventDatabase
    lateinit var placeDb: PlaceDatabase
    lateinit var userDb: UserDatabase

    var titlePut: TextView? = null
    var description: TextView? = null
    var datePut: TextView? = null
    var timePut: TextView? = null
    var textDetail:TextView? = null
    var photo: ImageView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        Log.println(Log.INFO, "title", intent.getStringExtra("Title").toString())

        titlePut = findViewById(R.id.TitleDetail)
        description = findViewById(R.id.DescriptionDetail)
        datePut = findViewById(R.id.DateDetail)
        timePut = findViewById(R.id.TimeDetail)
        textDetail = findViewById(R.id.textDetail)
        photo = findViewById(R.id.DetailPhoto)

        eventDb = Room.databaseBuilder(
            applicationContext,
            EventDatabase::class.java, "event_data"
        ).allowMainThreadQueries().build()

        placeDb = Room.databaseBuilder(
            applicationContext,
            PlaceDatabase::class.java, "place_data"
        ).allowMainThreadQueries().build()

        userDb = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "user_data"
        ).allowMainThreadQueries().build()

        val user = userDb.userDao().checkIfUserIsCurrent(true)

        val event = eventDb.eventDao().getEventByTitle(intent.getStringExtra("Title").toString(),user.userName)

        val hours = event[0].hour
        val minutes = event[0].minute - 1

        val currentTime =  String.format("%02d:%02d", hours, minutes);

        titlePut?.text = event[0].title
        description?.text = event[0].descriptionEvent
        datePut?.text = event[0].day.toString() +
                "." + event[0].month.toString() + "." +
                event[0].year.toString()
        timePut?.text = currentTime
        textDetail?.text = event[0].place

        when (event[0].place) {
            "Sasazu" -> {
                photo?.setBackgroundResource(R.drawable.sasazu_restaurant)
            }
            "Bar21" -> photo?.setBackgroundResource(R.drawable.bar21)
            "Nadrazka" -> photo?.setBackgroundResource(R.drawable.nadrazka)
            else -> photo?.setBackgroundResource(R.drawable.default_event)

        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    startActivity(Intent(this@EventDetail, Home::class.java))
                    Log.println(Log.INFO,"helper","i clicked home")
                }
                R.id.plus->{
                    startActivity(Intent(this@EventDetail, AddNewEvent::class.java))
                    Log.println(Log.INFO, "helper", "i clicked add event")
                }
                R.id.user-> {
                    startActivity(Intent(this@EventDetail, Profile::class.java))
                    Log.println(Log.INFO,"helper","i clicked user")
                }
            }
            true
        }
    }
}