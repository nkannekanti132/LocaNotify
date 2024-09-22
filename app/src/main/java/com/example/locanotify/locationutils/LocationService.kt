package com.example.locanotify.locationutils

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.locanotify.R
import com.example.locanotify.repository.NotificationRepository
import com.example.locanotify.room.Notification
import com.example.locanotify.room.NotificationDatabase
import com.example.locanotify.utils.LocationUtils
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var allNotifications : LiveData<List<Notification>>
    private lateinit var repository : NotificationRepository
    private lateinit var locations : List<Notification>


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val dao = NotificationDatabase.getDatabase(application).getNotificationDao()
        repository = NotificationRepository(dao)
        allNotifications = repository.allNotification

        allNotifications.observeForever( Observer { list ->
            list?.let {
                if(!it.isEmpty())
                {
                    locations = it

                }

            }
        })
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            .setAutoCancel(true)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var flag = 1
        locationClient
            .getLocationUpdates(5000)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude
                val lon = location.longitude
                var flag = 1
                for (lo in locations)
                {
                    Log.d("debug", lo.toString())
                    val isInRange = LocationUtils.isWithinRange(lo.latitude,
                        lo.longitude,lat,lon,0.3)

                    if(isInRange == true )
                    {
                        val updatedNotification = notification.setContentText(
                            lo.notificationDescription
                        ).setContentTitle(lo.notificationTitle)
                            .setOngoing(true)
                        notificationManager.notify(1, updatedNotification.build())
                        flag=0
                        break
                    }

                }
                if(flag == 1)
                {
                    val notification = NotificationCompat.Builder(this, "location")
                        .setContentTitle("Tracking location...")
                        .setContentText("")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setOngoing(true)
                        .setAutoCancel(true)
                        .setOngoing(true)
                    notificationManager.notify(1, notification.build())
                }


            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


}