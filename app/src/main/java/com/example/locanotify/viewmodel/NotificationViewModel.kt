package com.example.locanotify.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.locanotify.repository.NotificationRepository
import com.example.locanotify.room.Notification
import com.example.locanotify.room.NotificationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationViewModel (application: Application) :AndroidViewModel(application) {

    val allNotifications : LiveData<List<Notification>>
    val repository : NotificationRepository

    init {
        val dao = NotificationDatabase.getDatabase(application).getNotificationDao()
        repository = NotificationRepository(dao)
        allNotifications = repository.allNotification
    }

    fun deleteNotification (notification: Notification) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(notification)
    }


    fun updateNotification(notification: Notification) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(notification)
    }



    fun addNote(notification: Notification) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(notification)
    }
}
