package com.usp.holdinghands.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.R
import com.usp.holdinghands.activities.UserActivity
import com.usp.holdinghands.model.Gender
import com.usp.holdinghands.model.UserResponse
import com.usp.holdinghands.utils.EnumConverter
import com.usp.holdinghands.utils.JsonUtil

const val USER = "user"

class UserAdapter(
    var users: MutableList<UserResponse>,
    private val context: Context
) :
    RecyclerView.Adapter<UserAdapter.ConstraintLayoutViewHolder>() {

    inner class ConstraintLayoutViewHolder(val constraintLayout: ConstraintLayout) :
        RecyclerView.ViewHolder(constraintLayout), View.OnClickListener {

        override fun onClick(v: View?) {
            val intent = Intent(context, UserActivity::class.java).putExtra(
                USER,
                JsonUtil.toJson(users[adapterPosition])
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

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

        holder.constraintLayout.setOnClickListener(holder)

        holder.constraintLayout.findViewById<TextView>(R.id.user_name).text = user.name
        holder.constraintLayout.findViewById<TextView>(R.id.user_rating).text =
            holder.constraintLayout.context.getString(R.string.user_rating, user.rating.toString())
        holder.constraintLayout.findViewById<TextView>(R.id.user_age).text =
            holder.constraintLayout.context.getString(R.string.user_age, user.age.toString())
        holder.constraintLayout.findViewById<TextView>(R.id.user_distance).text =
            holder.constraintLayout.context.getString(
                R.string.user_distance,
                user.distance.toString()
            )

        holder.constraintLayout.findViewById<TextView>(R.id.user_help_types).text =
            if (user.isHelper) {
                EnumConverter.getHelpAsString(
                    EnumConverter.stringToEnumList(user.helpTypes!!)
                )
            } else {
                holder.constraintLayout.context.getString(R.string.filter_supported_radio)
            }

        val defaultImageId = if (user.gender == Gender.MALE) {
            if (user.isHelper) {
                "young_man"
            } else {
                "old_man"
            }
        } else {
            if (user.isHelper) {
                "young_woman"
            } else {
                "old_woman"
            }
        }

        val imageId = holder.constraintLayout.context.resources.getIdentifier(
            user.imageId ?: defaultImageId,
            "drawable",
            holder.constraintLayout.context.packageName
        )
        holder.constraintLayout.findViewById<ImageView>(R.id.user_image).setImageResource(imageId)
    }

    override fun getItemCount(): Int = users.size
}
