package com.example.alkoplan

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import com.example.alkoplan.data.user.UserDatabase

class SignInActivity : AppCompatActivity() {

    private var logInButton: Button? = null
    private var usernameField: EditText? = null
    private var passwordField: EditText? = null
    private var redirect: TextView? = null
    lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "user_data"
        ).allowMainThreadQueries().build()

        logInButton = findViewById(R.id.log_in_button)
        usernameField = findViewById(R.id.editTextUName)
        passwordField = findViewById(R.id.editTextPwd)
        redirect= findViewById(R.id.dontHaveAcc)

        redirect?.setOnClickListener{
            val intent = Intent(this@SignInActivity, Registry::class.java)
            startActivity(intent)
        }

        logInButton?.setOnClickListener {
            val rs = db.userDao().findUser(usernameField?.text.toString(), passwordField?.text.toString())
            if (rs != null) {
                Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_LONG).show()
                db.userDao().updateUser(true, rs.userName)
                val intent = Intent(this@SignInActivity, Loading::class.java)
                intent.putExtra("username", rs.userName)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Wrong combination", Toast.LENGTH_LONG).show()
                usernameField?.setTextColor(Color.RED)
                passwordField?.setTextColor(Color.RED)
            }
        }
    }
}