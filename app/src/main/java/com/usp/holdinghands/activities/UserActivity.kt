package com.usp.holdinghands.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.usp.holdinghands.R
import com.usp.holdinghands.adapter.IS_HISTORY_VIEW
import com.usp.holdinghands.adapter.IS_PENDING_VIEW
import com.usp.holdinghands.adapter.USER
import com.usp.holdinghands.controller.MatchController
import com.usp.holdinghands.model.*
import com.usp.holdinghands.utils.EnumConverter
import com.usp.holdinghands.utils.JsonUtil
import com.usp.holdinghands.utils.MaskEditUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {

    private lateinit var user: UserResponse
    private lateinit var matchController: MatchController

    private var isPendingView: Boolean = false
    private var isHistoryView: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        user = JsonUtil.fromJson(intent.extras!!.getString(USER)!!)
        matchController = MatchController(this)

        isPendingView = intent.extras!!.getBoolean(IS_PENDING_VIEW)
        isHistoryView = intent.extras!!.getBoolean(IS_HISTORY_VIEW)

        configureViews()
        configureButtons()
    }

    private fun configureViews() {
        val imageId = applicationContext.resources.getIdentifier(
            user.imageId ?: (if (user.gender == Gender.MALE) "lucas" else "heloise"),
            "drawable",
            applicationContext.packageName
        )

        findViewById<ImageView>(R.id.user_image).setImageResource(imageId)
        findViewById<TextView>(R.id.user_name).text = user.name
        findViewById<TextView>(R.id.user_rating).text = applicationContext.getString(R.string.user_rating, user.rating.toString())
        findViewById<TextView>(R.id.user_age).text = applicationContext.getString(R.string.user_age, user.age.toString())
        findViewById<TextView>(R.id.user_distance).text = getString(
            R.string.user_distance_long,
            user.distance.toString()
        )

        if (user.isHelper) {
            findViewById<TextView>(R.id.user_help_types).text = EnumConverter.getHelpAsString(
                EnumConverter.stringToEnumList(user.helpTypes!!)
            )

            if (user.numberOfHelps <= 1) {
                findViewById<TextView>(R.id.user_number_helps).text = applicationContext.getString(R.string.user_number_helps_singular, user.numberOfHelps.toString())
            } else {
                findViewById<TextView>(R.id.user_number_helps).text = applicationContext.getString(R.string.user_number_helps_plural, user.numberOfHelps.toString())
            }

        } else {
            findViewById<TextView>(R.id.user_help_types).text = getString(R.string.filter_supported_radio)
            findViewById<TextView>(R.id.user_number_helps).visibility = View.GONE
        }

        findViewById<TextView>(R.id.user_profession).text = user.profession

        when (user.gender) {
            Gender.FEMALE -> findViewById<TextView>(R.id.user_gender).text = applicationContext.getString(R.string.filter_female_switch)
            Gender.MALE -> findViewById<TextView>(R.id.user_gender).text = applicationContext.getString(R.string.filter_male_switch)
            Gender.BOTH -> findViewById<TextView>(R.id.user_gender).visibility = View.GONE
        }

        findViewById<TextView>(R.id.user_email).text = user.email
        findViewById<TextView>(R.id.user_phone).text = MaskEditUtil.mask(user.phone, MaskEditUtil.PHONE_MASK)

        if (isPendingView || isHistoryView) {
            setVisibilityOfContactViews(View.VISIBLE)
        } else {
            setVisibilityOfContactViews(View.GONE)
        }
    }

    private fun configureButtons() {
        findViewById<ImageButton>(R.id.back_button).setOnClickListener { finish() }

        findViewById<ImageButton>(R.id.report_button).setOnClickListener {
            val intent = Intent(this, ReportUserActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.user_send_invitation).setOnClickListener {
            sendInvite()
        }

        findViewById<RatingBar>(R.id.user_rating_bar).setOnRatingBarChangeListener { _, rating, _ ->
            findViewById<Button>(R.id.user_send_rating).isEnabled = rating > 0
        }

        findViewById<Button>(R.id.user_send_rating).setOnClickListener {
            Toast.makeText(applicationContext, applicationContext.getString(R.string.user_rating_success_message), Toast.LENGTH_LONG).show()
            findViewById<RatingBar>(R.id.user_rating_bar).setIsIndicator(true)
            it.isEnabled = false
        }
    }

    private fun sendInvite() {
        findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        findViewById<Button>(R.id.user_send_invitation).isEnabled = false

        matchController.sendInvite(user.userId, object : Callback<MatchResponse> {
            override fun onResponse(call: Call<MatchResponse>, response: Response<MatchResponse>) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                findViewById<Button>(R.id.user_send_invitation).isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(applicationContext, applicationContext.getString(R.string.send_invitation_sucessful_message), Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    //TODO: Show error message
                }
            }

            override fun onFailure(call: Call<MatchResponse>, t: Throwable) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                findViewById<Button>(R.id.user_send_invitation).isEnabled = true
                //TODO: Show error message
            }
        })
    }

    private fun setVisibilityOfContactViews(visibility: Int) {
        findViewById<TextView>(R.id.user_email).visibility = visibility
        findViewById<TextView>(R.id.user_phone).visibility = visibility
        findViewById<RatingBar>(R.id.user_rating_bar).visibility =
                if(isHistoryView) View.VISIBLE else View.GONE
        findViewById<Button>(R.id.user_send_rating).visibility =
                if(isHistoryView) View.VISIBLE  else View.GONE
        findViewById<Button>(R.id.user_send_invitation).visibility =
            if (visibility == View.GONE) View.VISIBLE else View.GONE
    }
}
