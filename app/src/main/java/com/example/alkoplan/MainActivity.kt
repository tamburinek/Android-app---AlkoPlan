package com.example.alkoplan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.room.Room
import com.example.alkoplan.data.InitData
import com.example.alkoplan.data.user.UserDatabase

class MainActivity : AppCompatActivity() {

    private var loginButton: Button? = null
    private var registrationButton: Button? = null
    lateinit var db: UserDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "user_data"
        ).allowMainThreadQueries().build()

        db.userDao().logoutAll(false, true)

        InitData().initData(applicationContext)
        loginButton = findViewById(R.id.login_button)
        registrationButton = findViewById(R.id.registration_button)

        loginButton?.setOnClickListener {
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        registrationButton?.setOnClickListener {
            val intent = Intent(this@MainActivity, Registry::class.java)
            startActivity(intent)
        }

    }
}