package com.usp.holdinghands.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.R
import com.usp.holdinghands.model.Notification
import kotlinx.android.synthetic.main.layout_notification.view.*

class NotificationAdapter(private val notifications: MutableList<Notification>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(constraintLayout: ConstraintLayout) :
            RecyclerView.ViewHolder(constraintLayout) {

        val imageView: ImageView = constraintLayout.notification_image
        val title: TextView = constraintLayout.notification_title
        val date: TextView = constraintLayout.notification_date

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val constraintLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_notification, parent, false) as ConstraintLayout

        return NotificationViewHolder(constraintLayout)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]

        holder.imageView.setImageResource(notification.image)
        holder.title.text = notification.title
        holder.date.text = notification.date
    }

    override fun getItemCount(): Int = notifications.size

}
