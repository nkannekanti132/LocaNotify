package com.example.locanotify.utils


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.locanotify.R
import com.example.locanotify.room.Notification

class NotificationRVAdapter(
    val context: Context,
    val notificationClickDeleteInterface: NotificationClickDeleteInterface,
    val notificationClickInterface: NotificationClickInterface
) :
    RecyclerView.Adapter<NotificationRVAdapter.ViewHolder>() {



    private val allNotifications = ArrayList<Notification>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val notificationTV = itemView.findViewById<TextView>(R.id.idTVNotification)
        val dateTV = itemView.findViewById<TextView>(R.id.idTVDate)
        val deleteIV = itemView.findViewById<ImageView>(R.id.idIVDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.notification_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.notificationTV.setText(allNotifications.get(position).notificationTitle)
        holder.dateTV.setText("Last Updated : " + allNotifications.get(position).timeStamp)

        holder.deleteIV.setOnClickListener {

            notificationClickDeleteInterface.onDeleteIconClick(allNotifications.get(position))
        }


        holder.itemView.setOnClickListener {
            notificationClickInterface.onNotificationClick(allNotifications.get(position))
        }
    }

    override fun getItemCount(): Int {
        return allNotifications.size
    }


    fun updateList(newList: List<Notification>) {

        allNotifications.clear()

        allNotifications.addAll(newList)

        notifyDataSetChanged()
    }
}

interface NotificationClickDeleteInterface {
    fun onDeleteIconClick(notification: Notification)
}

interface NotificationClickInterface {

    fun onNotificationClick(notification: Notification)
}
