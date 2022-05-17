package com.example.alkoplan

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.alkoplan.data.user.UserDatabase
import kotlinx.android.synthetic.main.activity_home.*

class Settings : AppCompatActivity() {

    private var logoutButton: Button? = null
    lateinit var userDb: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        userDb = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "user_data"
        ).allowMainThreadQueries().build()

        logoutButton = findViewById(R.id.logout_button)
        val rs = userDb.userDao().checkIfUserIsCurrent(true)
        logoutButton?.setOnClickListener {
            val a_builder: AlertDialog.Builder = AlertDialog.Builder(this@Settings)
            a_builder.setMessage("Do you want to leave us?")
                .setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    userDb.userDao().updateUser(false, rs.userName)
                    Toast.makeText(applicationContext, "Logged out", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@Settings, MainActivity::class.java))
                    Log.println(Log.INFO,"helper","User ${rs.userName} was logged out")
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            val alert: AlertDialog = a_builder.create()
            alert.setTitle("Betrayal")
            alert.show()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    startActivity(Intent(this@Settings, Home::class.java))
                    Log.println(Log.INFO,"helper","i clicked home")
                }
                R.id.plus->{
                    startActivity(Intent(this@Settings, AddNewEvent::class.java))
                    Log.println(Log.INFO, "helper", "i clicked add event")
                }
                R.id.user-> {
                    startActivity(Intent(this@Settings, Profile::class.java))
                    Log.println(Log.INFO,"helper","i clicked user")
                }
            }
            true
        }
    }
}