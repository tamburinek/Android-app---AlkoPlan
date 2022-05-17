package com.example.alkoplan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.room.Room
import com.example.alkoplan.data.event.EventDatabase
import com.example.alkoplan.data.place.PlaceDatabase
import com.example.alkoplan.data.user.UserDatabase
import kotlinx.android.synthetic.main.activity_home.*


class Home : AppCompatActivity() {

    private var infoSasazu: Button? = null
    private var infoBar21: Button? = null
    private var infoNadrazka: Button? = null
    private var nextEventTitle: TextView? = null
    private var nextEventView: ImageView? = null
    lateinit var eventDb: EventDatabase
    lateinit var userDb: UserDatabase
    lateinit var placeDb: PlaceDatabase
    private var nextEventRec : ConstraintLayout? = null

    @SuppressLint("Range", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "AlkoPlan"

        eventDb = Room.databaseBuilder(
            applicationContext,
            EventDatabase::class.java, "event_data"
        ).allowMainThreadQueries().build()

        userDb = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "user_data"
        ).allowMainThreadQueries().build()

        placeDb = Room.databaseBuilder(
            applicationContext,
            PlaceDatabase::class.java, "place_data"
        ).allowMainThreadQueries().build()

        infoSasazu = findViewById(R.id.susazuInfo)
        infoBar21 = findViewById(R.id.bar21Info)
        infoNadrazka = findViewById(R.id.nadrazkaInfo)
        nextEventTitle = findViewById(R.id.infoAboutLastEvent)
        nextEventView = findViewById(R.id.nextEventView)
        nextEventRec = findViewById(R.id.nextEventRec)

        val placeOfNextEvent = findViewById(R.id.PlaceOfNextEvent) as TextView
        val timeOfNextEvent = findViewById(R.id.TimeOfNextEvent) as TextView


        if (!eventDb.eventDao()
                .getLastUserEvent(userDb.userDao()
                    .checkIfUserIsCurrent(true).userName)
                .isEmpty()) {
            val event = eventDb.eventDao().getLastUserEvent(userDb.userDao().checkIfUserIsCurrent(true).userName).get(eventDb.eventDao().getLastUserEvent(userDb.userDao().checkIfUserIsCurrent(true).userName).size - 1)
            nextEventTitle!!.text = event.title

            when (event.place) {
                "Sasazu" -> nextEventView?.setImageResource(R.drawable.sasazu_restaurant)
                "Bar21" -> nextEventView?.setImageResource(R.drawable.bar21)
                "Nadrazka" -> nextEventView?.setImageResource(R.drawable.nadrazka)
            }

            placeOfNextEvent.text = event.place

            val timeInfo = String.format("%02d:%02d", event.hour, event.minute-1);
            timeOfNextEvent.text = event.day.toString() +  "." +
                    event.month.toString() + "." +
                    event.year.toString() + " - " + timeInfo
        }

        nextEventRec?.setOnClickListener {
            if (nextEventTitle?.text != "Your next event will be right here"){
                val intent = Intent(this@Home, EventDetail::class.java)
                intent.putExtra("Title", nextEventTitle?.text)
                startActivity(intent)
            }else{
                Toast.makeText(applicationContext, "You do not have any event to display", Toast.LENGTH_LONG).show()
            }
        }

        infoSasazu?.setOnClickListener {
            val intent = Intent(this@Home, PlaceDetail::class.java)
            intent.putExtra("place", "Sasazu")
            startActivity(intent)
        }

        infoBar21?.setOnClickListener {
            val intent = Intent(this@Home, PlaceDetail::class.java)
            intent.putExtra("place", "Bar21")
            startActivity(intent)
        }

        infoNadrazka?.setOnClickListener {
            val intent = Intent(this@Home, PlaceDetail::class.java)
            intent.putExtra("place", "Nadrazka")
            startActivity(intent)
        }



        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    Log.println(Log.INFO,"helper","i clicked home")
                }
                R.id.plus->{
                    startActivity(Intent(this@Home, AddNewEvent::class.java))
                    Log.println(Log.INFO, "helper", "i clicked add event")
                }
                R.id.user-> {
                    startActivity(Intent(this@Home, Profile::class.java))
                    Log.println(Log.INFO,"helper","i clicked user")
                }
            }
            true
        }
}
}