package com.example.alkoplan

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.alkoplan.data.event.Event
import com.example.alkoplan.data.event.EventDatabase
import com.example.alkoplan.data.user.User
import com.example.alkoplan.data.user.UserDatabase
import com.example.alkoplan.databinding.ActivityAddNewEventBinding
import kotlinx.android.synthetic.main.activity_add_new_event.*
import java.text.SimpleDateFormat
import java.util.*


class AddNewEvent : AppCompatActivity() {
    val myCalendar: Calendar = Calendar.getInstance()
    var DateInput: EditText? = null
    var TimeInput: EditText? = null
    var titleInput: EditText? = null
    var descriptionInput: EditText? = null
    var placeText:EditText? = null
    var addButton: Button? = null
    private lateinit var binding: ActivityAddNewEventBinding

    lateinit var eventDb: EventDatabase
    lateinit var userDb: UserDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        DateInput = findViewById<View>(com.example.alkoplan.R.id.birthday) as EditText
        TimeInput = findViewById<View>(com.example.alkoplan.R.id.TimeInputs) as EditText
        titleInput = findViewById<View>(com.example.alkoplan.R.id.TitleInputs) as EditText
        descriptionInput = findViewById<View>(com.example.alkoplan.R.id.DescriptionInputs) as EditText
        addButton = findViewById(com.example.alkoplan.R.id.add_event_button)

        eventDb = Room.databaseBuilder(
            applicationContext,
            EventDatabase::class.java, "event_data"
        ).allowMainThreadQueries().build()

        userDb = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "user_data"
        ).allowMainThreadQueries().build()


        val date =
            OnDateSetListener { view, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }

        val time =
            TimePickerDialog.OnTimeSetListener {view, hour, minute ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hour)
                myCalendar.set(Calendar.MINUTE, minute)
                updateLabel2()
            }


        DateInput!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                DatePickerDialog(
                    this@AddNewEvent,
                    date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        TimeInput!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                TimePickerDialog(
                    this@AddNewEvent,
                    time,
                    myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE),
                    true
                ).show()
            }
        })

        if (intent.getStringExtra("Place") == "Bar21"){
            placeText = findViewById<View>(com.example.alkoplan.R.id.placeInput) as EditText
            placeText?.setText("Bar21")
            placeText?.isFocusable = false
        }

        else if (intent.getStringExtra("Place") == "Sasazu"){
            placeText = findViewById<View>(com.example.alkoplan.R.id.placeInput) as EditText
            placeText?.setText("Sasazu")
            placeText?.isFocusable = false
        }

        else if (intent.getStringExtra("Place") == "Nadrazka"){
            placeText = findViewById<View>(com.example.alkoplan.R.id.placeInput) as EditText
            placeText?.setText("Nadrazka")
            placeText?.isFocusable = false
        }

        var user: User
        user = userDb.userDao().checkIfUserIsCurrent(true)

        placeText = findViewById(com.example.alkoplan.R.id.placeInput)

        addButton?.setOnClickListener {

            var helper = eventDb.eventDao().getEventByTitle(titleInput?.text.toString(), user.userName)

            if(!helper.isEmpty()){
                Toast.makeText(applicationContext, "You already used this title", Toast.LENGTH_LONG).show()
                titleInput?.setTextColor(Color.RED)
            }

            else if(titleInput?.text.toString() == ""){
                Toast.makeText(applicationContext, "You have to choose title", Toast.LENGTH_LONG).show()
                titleInput?.setHintTextColor(Color.RED)
            }

            else{
                eventDb.eventDao().addEvent(Event(
                    0, titleInput?.text.toString(), descriptionInput?.text.toString(),user.userName, placeText?.text.toString(),
                    myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH) + 1,
                    myCalendar.get(Calendar.DAY_OF_MONTH),myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE) + 1,
                ))
                Log.println(Log.INFO,"year",(myCalendar.get(Calendar.YEAR)).toString())
                Log.println(Log.INFO,"month",(myCalendar.get(Calendar.MONTH) + 1).toString())
                Log.println(Log.INFO,"day",(myCalendar.get(Calendar.DAY_OF_MONTH)).toString())
                Log.println(Log.INFO,"hour",(myCalendar.get(Calendar.HOUR_OF_DAY)).toString())
                Log.println(Log.INFO,"minute",(myCalendar.get(Calendar.MINUTE)).toString())

                scheduleNotification()

                Toast.makeText(applicationContext, "Event created", Toast.LENGTH_LONG).show()
                var intentik = Intent(this@AddNewEvent, EventDetail::class.java)
                intentik.putExtra("Title", titleInput?.text.toString())
                startActivity(intentik)
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                com.example.alkoplan.R.id.home-> {
                    startActivity(Intent(this@AddNewEvent, Home::class.java))
                    Log.println(Log.INFO,"helper","i clicked home")
                }
                com.example.alkoplan.R.id.plus->{
                    Log.println(Log.INFO, "helper", "i clicked add event")
                }
                com.example.alkoplan.R.id.user-> {
                    startActivity(Intent(this@AddNewEvent, Profile::class.java))
                    Log.println(Log.INFO,"helper","i clicked user")
                }
            }
            true
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = "Dude! You have 1 more planned event: " + binding.TitleInputs.text.toString()
        val description = "Description: " + binding.DescriptionInputs.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, description)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = 10000
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.toLong(),
            pendingIntent
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notification channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateLabel() {
        val myFormat = "dd.MM.yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.GERMAN)
        DateInput?.setText(dateFormat.format(myCalendar.getTime()))
    }

    private fun updateLabel2() {
        val myFormat = "HH:mm"
        val dateFormat = SimpleDateFormat(myFormat, Locale.GERMAN)
        TimeInput?.setText(dateFormat.format(myCalendar.getTime()))
    }
}

