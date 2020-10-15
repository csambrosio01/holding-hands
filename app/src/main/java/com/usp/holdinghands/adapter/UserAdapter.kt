package com.usp.holdinghands.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.R
import com.usp.holdinghands.model.User
import com.usp.holdinghands.model.getHelpAsString


class UserAdapter(private val users: MutableList<User>) :
    RecyclerView.Adapter<UserAdapter.ConstraintLayoutViewHolder>() {

    class ConstraintLayoutViewHolder(val constraintLayout: ConstraintLayout) :
        RecyclerView.ViewHolder(constraintLayout)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConstraintLayoutViewHolder {
        val constraintLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_user_card, parent, false) as ConstraintLayout

        return ConstraintLayoutViewHolder(constraintLayout)
    }

    override fun onBindViewHolder(holder: ConstraintLayoutViewHolder, position: Int) {
        val user = users[position]

        holder.constraintLayout.findViewById<TextView>(R.id.user_name).text = user.name
        holder.constraintLayout.findViewById<TextView>(R.id.user_age).text =
            holder.constraintLayout.context.getString(R.string.user_age, user.age.toString())
        holder.constraintLayout.findViewById<TextView>(R.id.user_distance).text =
            holder.constraintLayout.context.getString(
                R.string.user_distance,
                user.distance.toString()
            )
        holder.constraintLayout.findViewById<TextView>(R.id.user_help_types).text =
            user.getHelpAsString()

        val imageId = holder.constraintLayout.context.resources.getIdentifier(
            user.image,
            "drawable",
            holder.constraintLayout.context.packageName
        )
        holder.constraintLayout.findViewById<ImageView>(R.id.user_image).setImageResource(imageId)
    }

    override fun getItemCount(): Int = users.size
}
