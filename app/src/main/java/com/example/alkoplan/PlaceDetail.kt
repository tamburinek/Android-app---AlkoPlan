package com.example.alkoplan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.room.Room
import com.example.alkoplan.data.place.PlaceDatabase
import kotlinx.android.synthetic.main.activity_home.*

class PlaceDetail : AppCompatActivity() {

    private var visitButton: Button? = null
    lateinit var placeDb: PlaceDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        placeDb = Room.databaseBuilder(
            applicationContext,
            PlaceDatabase::class.java, "place_data"
        ).allowMainThreadQueries().build()


        val nemaInput = findViewById(R.id.textViewDetail) as TextView
        val photoInput = findViewById(R.id.PlacePhotoDetail) as ImageView
        val ratingInput = findViewById(R.id.ratingBarDetail) as RatingBar
        val descriptionInput = findViewById(R.id.textDescription) as TextView
        val adressInput = findViewById(R.id.textAddress) as TextView
        visitButton = findViewById(R.id.visitDetail)


        val place = placeDb.placeDao().getPlaceByName(intent.getStringExtra("place").toString())
        Log.println(Log.INFO, "place",intent.getStringExtra("place").toString())
        Log.println(Log.INFO, "place",place.toString())
        nemaInput.text = place[0].placeName
        ratingInput.rating = place[0].rating
        descriptionInput.text = place[0].description
        adressInput.text = place[0].address

        when (intent.getStringExtra("place")) {
            "Sasazu" -> {
                photoInput.setBackgroundResource(R.drawable.sasazu_restaurant)
            }
            "Bar21" -> photoInput.setBackgroundResource(R.drawable.bar21)
            "Nadrazka" -> photoInput.setBackgroundResource(R.drawable.nadrazka)
            else -> photoInput.setBackgroundResource(R.drawable.default_event)

        }

        val intentDetailTo = Intent(this@PlaceDetail, AddNewEvent::class.java)
        intentDetailTo.putExtra("Place", place[0].placeName)

        visitButton?.setOnClickListener {
            startActivity(intentDetailTo)
        }

        val intentHome = Intent(this@PlaceDetail, Home::class.java)
        val intentAdd = Intent(this@PlaceDetail, AddNewEvent::class.java)
        val intentUser = Intent(this@PlaceDetail, Profile::class.java)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> startActivity(intentHome)
                R.id.plus-> {
                    startActivity(intentAdd)
                }
                R.id.user-> startActivity(intentUser)
            }
            true
        }
    }
}