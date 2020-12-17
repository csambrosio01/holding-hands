package com.usp.holdinghands.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.R
import com.usp.holdinghands.activities.UserActivity
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.Gender
import com.usp.holdinghands.model.MatchResponse
import com.usp.holdinghands.model.MatchStatus
import com.usp.holdinghands.utils.EnumConverter
import com.usp.holdinghands.utils.JsonUtil
import kotlinx.android.synthetic.main.layout_user_card.view.*

const val MATCH = "match"
const val IS_PENDING_VIEW = "is_pending_view"
const val IS_HISTORY_VIEW = "is_history_view"

interface OnItemClickListener {
    fun onAccept(position: Int)
    fun onDeny(position: Int)
}

class MatchAdapter(
    private val matchs: MutableList<MatchResponse>,
    private val context: Context,
    private val isPendingView: Boolean = false,
    private val isHistoryView: Boolean = false,
    private val listener: OnItemClickListener? = null
) : RecyclerView.Adapter<MatchAdapter.ConstraintLayoutViewHolder>() {

    inner class ConstraintLayoutViewHolder(val constraintLayout: ConstraintLayout) :
        RecyclerView.ViewHolder(constraintLayout), View.OnClickListener {

        override fun onClick(v: View?) {
            val intent = Intent(context, UserActivity::class.java).putExtra(
                MATCH,
                JsonUtil.toJson(matchs[adapterPosition])
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
    ): MatchAdapter.ConstraintLayoutViewHolder {
        val constraintLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_user_card, parent, false) as ConstraintLayout

        return ConstraintLayoutViewHolder(constraintLayout)
    }

    override fun onBindViewHolder(holder: ConstraintLayoutViewHolder, position: Int) {
        val match = matchs[position]
        val currentUser = UserController(context).getLoggedUser()!!
        val user =
            if (match.userReceived.userId == currentUser.userId) match.userSent else match.userReceived

        holder.constraintLayout.setOnClickListener(holder)

        holder.constraintLayout.findViewById<TextView>(R.id.user_name).text =
            if (match.userReceived.userId == currentUser.userId) {
                context.getString(R.string.received_from, user.name)
            } else {
                context.getString(R.string.sent_to, user.name)
            }

        val userRating = holder.constraintLayout.findViewById<TextView>(R.id.user_rating)
        userRating.text =
            holder.constraintLayout.context.getString(R.string.user_rating, user.rating.toString())
        if (user.rating == 0.0) {
            userRating.visibility = View.GONE
        }

        holder.constraintLayout.findViewById<TextView>(R.id.user_age).text =
            holder.constraintLayout.context.getString(
                R.string.user_age,
                user.age.toString()
            )
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

        val matchStatusTextView = holder.constraintLayout.findViewById<TextView>(R.id.match_status)
        when {
            isPendingView -> {
                holder.constraintLayout.findViewById<ConstraintLayout>(R.id.help_buttons).visibility =
                    View.VISIBLE

                holder.constraintLayout.button_accept.setOnClickListener {
                    listener?.onAccept(position)
                }

                holder.constraintLayout.button_deny.setOnClickListener {
                    listener?.onDeny(position)
                }
            }
            isHistoryView -> {

                val color: Int
                val statusText: String

                when (match.status) {
                    MatchStatus.ACCEPT -> {
                        color = Color.parseColor("#007A1A")
                        statusText = context.getString(R.string.accepted)
                    }
                    MatchStatus.REJECT -> {
                        color = Color.RED
                        statusText = context.getString(R.string.rejected)
                    }
                    MatchStatus.DONE -> {
                        color = Color.parseColor("#007A1A")
                        statusText = context.getString(R.string.done)
                    }
                    MatchStatus.PENDING -> {
                        color = Color.parseColor("#FFD700")
                        statusText = context.getString(R.string.pending_fragment_title)
                    }
                    else -> {
                        color = Color.GRAY
                        statusText = ""
                    }
                }

                var text = context.getString(R.string.status, "<color>$statusText</color>")

                val start = text.indexOf("<color>")
                text = text.replace("<color>", "")

                val end = text.indexOf("</color>")
                text = text.replace("</color>", "")

                val span = SpannableString(text)

                span.setSpan(
                    ForegroundColorSpan(color),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                matchStatusTextView.visibility =
                    View.VISIBLE
                matchStatusTextView.text = span
            }
            else -> {
                holder.constraintLayout.findViewById<ConstraintLayout>(R.id.help_buttons).visibility =
                    View.GONE
                matchStatusTextView.visibility =
                    View.GONE
            }
        }
    }

    override fun getItemCount(): Int = matchs.size
}
