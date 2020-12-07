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
import kotlinx.android.synthetic.main.layout_user_card.view.*

const val USER = "user"

interface OnItemClickListener {
    fun onAccept(position: Int)
    fun onDeny(position: Int)
}

const val IS_PENDING_VIEW = "is_pending_view"
const val IS_HISTORY_VIEW = "is_history_view"

class UserAdapter(
    private val users: MutableList<UserResponse>,
    private val context: Context,
    private val isPendingView: Boolean = false,
    private val isHistoryView: Boolean = false,
    private val listener: OnItemClickListener? = null
) :
    RecyclerView.Adapter<UserAdapter.ConstraintLayoutViewHolder>() {

    inner class ConstraintLayoutViewHolder(val constraintLayout: ConstraintLayout) :
        RecyclerView.ViewHolder(constraintLayout), View.OnClickListener {

        override fun onClick(v: View?) {
            val intent = Intent(context, UserActivity::class.java).putExtra(
                USER,
                JsonUtil.toJson(users[adapterPosition])
            )
                .putExtra(IS_PENDING_VIEW, isPendingView)
                    .putExtra(IS_HISTORY_VIEW, isHistoryView)
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

        val imageId = holder.constraintLayout.context.resources.getIdentifier(
            user.imageId ?: (if (user.gender == Gender.MALE) "lucas" else "heloise"),
            "drawable",
            holder.constraintLayout.context.packageName
        )
        holder.constraintLayout.findViewById<ImageView>(R.id.user_image).setImageResource(imageId)

        if (isPendingView) {
            holder.constraintLayout.findViewById<ConstraintLayout>(R.id.help_buttons).visibility =
                View.VISIBLE

            holder.constraintLayout.button_accept.setOnClickListener {
                listener?.onAccept(position)
            }

            holder.constraintLayout.button_deny.setOnClickListener {
                listener?.onDeny(position)
            }
        } else {
            holder.constraintLayout.findViewById<ConstraintLayout>(R.id.help_buttons).visibility =
                View.GONE
        }
    }

    override fun getItemCount(): Int = users.size
}
