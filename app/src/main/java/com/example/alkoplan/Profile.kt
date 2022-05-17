package com.example.alkoplan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.alkoplan.data.event.Event
import com.example.alkoplan.data.event.EventDatabase
import com.example.alkoplan.data.user.User
import com.example.alkoplan.data.user.UserDatabase
import com.example.alkoplan.databinding.ActivityProfileBinding
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime


class Profile : AppCompatActivity() {

    lateinit var userNameView: TextView
    lateinit var userDb: UserDatabase
    lateinit var eventDb: EventDatabase
    lateinit var binding: ActivityProfileBinding
    private val adapter = EventAdapter()
    private var rs: User? = null
    private var events: LiveData<List<Event>>? = null
    private var changeAvatarButton: Button? = null
    lateinit var profilePicture: ImageView
    private var currentPhotoPath: String? = null
    private var photo: Bitmap? = null
    private var settingsButton: Button? = null
    private var eventInfo: CardView? = null


    @SuppressLint("QueryPermissionsNeeded")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDb = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "user_data"
        ).allowMainThreadQueries().build()

        eventDb = Room.databaseBuilder(
            applicationContext,
            EventDatabase::class.java, "event_data"
        ).allowMainThreadQueries().build()
        userNameView = findViewById(R.id.username)
        profilePicture = findViewById(R.id.profile_image)
        changeAvatarButton = findViewById(R.id.changingAvatar)
        settingsButton = findViewById(R.id.settings)
        eventInfo = findViewById(R.id.eventCardView)
        rs = userDb.userDao().checkIfUserIsCurrent(true)

        userNameView.text = rs!!.userName

        changeAvatarButton?.setOnClickListener {
           if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
           } else {
               val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
               startActivityForResult(cameraIntent, 1888)
           }
        }

        settingsButton?.setOnClickListener {
            startActivity(Intent(this@Profile, Settings::class.java))
            Log.println(Log.INFO,"helper","i clicked settings")
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    startActivity(Intent(this@Profile, Home::class.java))
                    Log.println(Log.INFO,"helper","i clicked home")
                }
                R.id.plus->{
                    startActivity(Intent(this@Profile, AddNewEvent::class.java))
                    Log.println(Log.INFO, "helper", "i clicked add event")
                }
                R.id.user-> {
                    Log.println(Log.INFO,"helper","i clicked user")
                }
            }
            true
        }
        init()
    }

    override fun onStart() {
        super.onStart()
        if (!rs!!.photoUrl.equals("")) profilePicture.setImageURI(
            Uri.parse(userDb.userDao().checkIfUserIsCurrent(true).photoUrl))
        Log.d("Info", "In the onStart() event");
    }


    private fun init() {
        binding.apply {
            eventRecyclerView.layoutManager = LinearLayoutManager(this@Profile)
            eventRecyclerView.adapter = adapter
            val string = rs!!.userName
            var eventList: MutableList<Event>? = null
            events = eventDb.eventDao().getAllUserEvents(string)

            events!!.observe(this@Profile, Observer{
                eventList = it as MutableList<Event>?
                for (item in eventList!!) {
                    adapter.addEvent(item)
                }
            })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1888) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 100)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1888 && resultCode == RESULT_OK) {
            val bundle = data!!.extras
            photo = bundle!!.get("data") as Bitmap?
            profilePicture.setImageBitmap(photo as Bitmap?)
            savePhotoToStorage(photo!!)
        }
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun savePhotoToStorage(imageToSave: Bitmap) {

        val externalStorageState = Environment.getExternalStorageState()
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absoluteFile
            val file = File.createTempFile(
                "${LocalDateTime.now()}", /* prefix */
                ".jpg", /* suffix */
                storageDirectory /* directory */
            ).apply {
                currentPhotoPath?.let {
                    File(it).delete()
                }
                currentPhotoPath = absolutePath
            }
            if (file.exists()) file.delete()
            try {
                val out = FileOutputStream(file)
                imageToSave.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            userDb.userDao().updateProfilePhotoUri(Uri.parse(file.absolutePath).toString(), rs!!.userName)
            Toast.makeText(this, "photo changed", Toast.LENGTH_LONG).show()
        }
    }
}