package com.example.alkoplan

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import com.example.alkoplan.data.user.User
import com.example.alkoplan.data.user.UserDatabase
import java.util.*
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.widget.TextView
import java.text.SimpleDateFormat

class Registry : AppCompatActivity() {

    val myCalendar: Calendar = Calendar.getInstance()
    private var usernameField: EditText? = null
    private var passwordField: EditText? = null
    private var birthdayField: EditText? = null
    private var registryButton: Button? = null
    private var passwordSecond: EditText? = null
    private var redirect: TextView? = null
    lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registry)

        db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "user_data"
        ).allowMainThreadQueries().build()

        usernameField = findViewById(R.id.editTextUName)
        passwordField = findViewById(R.id.editTextPwd)
        passwordSecond = findViewById(R.id.editTextPwd2)
        birthdayField = findViewById(R.id.editTextBirthday)
        registryButton = findViewById(R.id.registry_button)
        redirect= findViewById(R.id.haveAccount)

        val date =
            OnDateSetListener { view, year, month, day ->
                view
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }

        birthdayField!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                DatePickerDialog(
                    this@Registry,
                    android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                    date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        redirect?.setOnClickListener{
            val intent = Intent(this@Registry, SignInActivity::class.java)
            startActivity(intent)
        }


        registryButton?.setOnClickListener {
            val userName = usernameField?.text.toString()
            val password = passwordField?.text.toString()
            val password2 = passwordSecond?.text.toString()

            usernameField?.setTextColor(Color.WHITE)
            passwordField?.setTextColor(Color.WHITE)
            passwordSecond?.setTextColor(Color.WHITE)

            if (!db.userDao().findUserByName(userName).isEmpty()){
                Toast.makeText(this, "This username already exists", Toast.LENGTH_LONG).show()
                usernameField?.setTextColor(Color.RED)
            }

            else if (password != password2 ){
                Toast.makeText(this, "Password should match", Toast.LENGTH_LONG).show()
                passwordField?.setTextColor(Color.RED)
                passwordSecond?.setTextColor(Color.RED)
            }

            else if (!isUser18Older(myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))){
                Toast.makeText(this, "You have to be older 18 years", Toast.LENGTH_LONG).show()
                birthdayField?.setTextColor(Color.RED)
            }

            else {
                insertDataToDatabase()
            }
        }
    }

    private fun insertDataToDatabase() {
        val userName = usernameField?.text.toString()
        val birthDay = birthdayField?.text.toString()
        val password = passwordField?.text.toString()


        if (inputCheck(userName, password, birthDay)) {
            val user = User(0, userName, birthDay, password, "", false)
            db.userDao().addUser(user)
            db.userDao().updateUser(true, userName)
            Toast.makeText(this, "Successfully created!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@Registry, Loading::class.java)
            intent.putExtra("username", userName)
            startActivity(intent)

        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(userName: String, password: String, birthday: String): Boolean {
        return !(TextUtils.isEmpty(userName)
                && TextUtils.isEmpty(password)
                && TextUtils.isEmpty(birthday))
    }

    private fun updateLabel() {
        val myFormat = "dd.MM.yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.ENGLISH)
        birthdayField?.setText(dateFormat.format(myCalendar.getTime()))
    }

    private fun isUser18Older(year : Int, month : Int, day: Int): Boolean {
        val c1 = Calendar.getInstance()
        c1[year, month, day, 0] = 0
        val c2 = Calendar.getInstance()
        var diff = c2[Calendar.YEAR] - c1[Calendar.YEAR]
        if (c1[Calendar.MONTH] > c2[Calendar.MONTH] ||
            c1[Calendar.MONTH] == c2[Calendar.MONTH] && c1[Calendar.DATE] > c2[Calendar.DATE]
        ) {
            diff--
        }
        return diff >= 18
    }
}