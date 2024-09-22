package com.example.locanotify.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notificationTable")
class Notification(@ColumnInfo(name = "title") val notificationTitle:String, @ColumnInfo(name = "description") val notificationDescription:String, @ColumnInfo(name = "timestamp") val timeStamp:String, @ColumnInfo(name = "latitude") val latitude:Double, @ColumnInfo(name = "longitude") val longitude:Double){
    @PrimaryKey(autoGenerate = true) var id = 0
}
