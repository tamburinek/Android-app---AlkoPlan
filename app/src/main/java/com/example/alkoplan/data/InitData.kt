package com.example.alkoplan.data

import android.content.Context
import androidx.room.Room
import com.example.alkoplan.data.event.EventDatabase
import com.example.alkoplan.data.place.Place
import com.example.alkoplan.data.place.PlaceDatabase
import com.example.alkoplan.data.user.UserDatabase

class InitData {

    lateinit var placeDb: PlaceDatabase

    fun initData(applicationContext: Context) {

        placeDb = Room.databaseBuilder(
            applicationContext,
            PlaceDatabase::class.java, "place_data"
        ).allowMainThreadQueries().build()

        placeDb.placeDao().addPlace(Place(
            0,
            "This Bar is placed in the middle of beautiful\n" +
                    "        city of Prague. Very cheap prices and\n" +
                    "        beautiful bartenders will force you to love\n" +
                    "        this place",
            "Sasazu",
            4F,
            "drawable://sasazu_restaurant/sasazu_restaurant.png",
            3,
            "306/13, Holešovice, Bubenské nábř., 170 00 Praha 7"
        ))

        placeDb.placeDao().addPlace(Place(
            0,
            "Come and visit one of the oldest gay bars in the center of Prague\n" +
                    "        with a wide range of alcohol. We offer a quiet and pleasant\n" +
                    "        environment ideal as the beginning of a party evening.",
        "Bar21",
            3.5f,
            "drawable://bar21/bar21.png",
            3,
            "21, Římská 419, Vinohrady, 120 00 Praha 2"
        ))

        placeDb.placeDao().addPlace(
            Place(
            0,
            "Legendary railway station in Prague. On the tap Pilsen and Staropramen.\n" +
                    "        To eat things from the grill. In addition to the indoor seating,\n" +
                    "        there are two gardens, one on the side of the building and the other\n" +
                    "        smaller directly on the platform.",
            "Nadrazka",
                5f,
                "drawable://nadrazka/nadrazka.png",
                3,
                "Václavkova 1, 160 00 Praha 6"
        ))

    }

}