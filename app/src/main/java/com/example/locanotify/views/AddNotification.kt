package com.example.locanotify.views

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.http.UrlRequest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.locanotify.R
import com.example.locanotify.room.Notification
import com.example.locanotify.viewmodel.NotificationViewModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.text.SimpleDateFormat
import java.util.*

class AddNotification : AppCompatActivity() {
    lateinit var notificationTitleEdt: EditText
    lateinit var notificationEdt: EditText
    lateinit var notificationLatitude : EditText
    lateinit var notificationLongitude : EditText
    lateinit var saveBtn: Button
    lateinit var addBtn: Button



    lateinit var viewModal: NotificationViewModel
    var notificationID = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notification)
        Places.initialize(applicationContext, "AIzaSyD_OebZjGxFltaZNbFmIyvy5p6zzOTnG6I")
        val placesClient = Places.createClient(this)


        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NotificationViewModel::class.java)

        notificationTitleEdt = findViewById(R.id.idEdtNotificationName)
        notificationEdt = findViewById(R.id.idEdtNotificationDesc)
        saveBtn = findViewById(R.id.idBtn)
        notificationLatitude = findViewById(R.id.idLatitude)
        notificationLongitude = findViewById(R.id.idLongitude)
        addBtn = findViewById(R.id.button)

        addBtn.setOnClickListener{
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startActivityForResult(intent,1)
        }

        val notificationType = intent.getStringExtra("NotificationType")
        if (notificationType.equals("Edit")) {

            val notificationTitle = intent.getStringExtra("NotificationTitle")
            val notificationDescription = intent.getStringExtra("NotificationDescription")
            val notificationLatitude1 = intent.getStringExtra("NotificationLatitude")
            val notificationLongitude1= intent.getStringExtra("NotificationLongitude")
            notificationID = intent.getIntExtra("NotificationId", -1)
            saveBtn.setText("Update notification")
            notificationTitleEdt.setText(notificationTitle)
            notificationEdt.setText(notificationDescription)
            notificationLatitude.setText(notificationLatitude1)
            notificationLongitude.setText(notificationLongitude1)

        } else {
            saveBtn.setText("Save Notification")
        }

        saveBtn.setOnClickListener {

            val notificationTitle = notificationTitleEdt.text.toString()
            val notificationDescription = notificationEdt.text.toString()
            val notificationLatitude = notificationLatitude.text.toString().toDouble()
            val notificationLongitude= notificationLongitude.text.toString().toDouble()

            if (notificationType.equals("Edit")) {
                if (notificationTitle.isNotEmpty() && notificationDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    val updatedNotification = Notification(notificationTitle, notificationDescription, currentDateAndTime, notificationLatitude, notificationLongitude)
                    updatedNotification.id = notificationID
                    viewModal.updateNotification(updatedNotification)
                    Toast.makeText(this, "Notification Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (notificationTitle.isNotEmpty() && notificationDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    viewModal.addNote(Notification(notificationTitle, notificationDescription, currentDateAndTime, notificationLatitude, notificationLongitude))
                    Toast.makeText(this, "$notificationTitle Added", Toast.LENGTH_LONG).show()
                }
            }

            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data)
                val lat = place.latLng.latitude.toString()
                val log = place.latLng.longitude.toString()
                notificationLatitude.setText(lat)
                notificationLongitude.setText(log)
                Log.i(TAG, "Place: " + place.name + ", " + place.id+" "+place.latLng)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status: Status = Autocomplete.getStatusFromIntent(data)
                Log.i(TAG, status.toString())
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}