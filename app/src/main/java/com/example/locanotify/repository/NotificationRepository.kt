package com.example.locanotify.repository

import androidx.lifecycle.LiveData
import com.example.locanotify.room.Notification
import com.example.locanotify.room.NotificationDAO

class NotificationRepository(private val notificationDAO: NotificationDAO) {


    val allNotification: LiveData<List<Notification>> = notificationDAO.getAllNotes()

    suspend fun insert(notification: Notification) {
        notificationDAO.insert(notification)
    }

    suspend fun delete(notification: Notification){
        notificationDAO.delete(notification)
    }

    suspend fun update(notification: Notification){
        notificationDAO.update(notification)
    }
}