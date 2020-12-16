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
import com.usp.holdinghands.adapter.MATCH
import com.usp.holdinghands.adapter.USER
import com.usp.holdinghands.controller.MatchController
import com.usp.holdinghands.controller.UserController
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
    private lateinit var userController: UserController

    private var match: MatchResponse? = null
    private var userReceived: Boolean = false
    private var isPendingView: Boolean = false
    private var isHistoryView: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        matchController = MatchController(this)
        userController = UserController(this)

        if (intent.extras?.getString(USER) != null) {
            user = JsonUtil.fromJson(intent.extras!!.getString(USER)!!)
        } else {
            match = JsonUtil.fromJson(intent.extras!!.getString(MATCH)!!)
            userReceived = match!!.userReceived.userId == userController.getLoggedUser()!!.userId
            user = if (userReceived) match!!.userSent else match!!.userReceived
        }

        isPendingView = intent.extras!!.getBoolean(IS_PENDING_VIEW)
        isHistoryView = intent.extras!!.getBoolean(IS_HISTORY_VIEW)

        getRate()
        configureViews()
        configureButtons()
    }

    private fun configureViews() {
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

        val imageId = applicationContext.resources.getIdentifier(
            user.imageId ?: defaultImageId,
            "drawable",
            applicationContext.packageName
        )

        findViewById<ImageView>(R.id.user_image).setImageResource(imageId)
        findViewById<TextView>(R.id.user_name).text = user.name

        val userRating = findViewById<TextView>(R.id.user_rating)
        userRating.text = applicationContext.getString(R.string.user_rating, user.rating.toString())
        if (user.rating == 0.0) {
            userRating.visibility = View.GONE
        }

        findViewById<TextView>(R.id.user_age).text = applicationContext.getString(R.string.user_age, user.age.toString())
        findViewById<TextView>(R.id.user_distance).text = getString(
            R.string.user_distance_long,
            user.distance.toString()
        )

        if (user.isHelper) {
            findViewById<TextView>(R.id.user_help_types).text = EnumConverter.getHelpAsString(
                EnumConverter.stringToEnumList(user.helpTypes!!)
            )

            when (user.numberOfHelps) {
                0 -> findViewById<TextView>(R.id.user_number_helps).text = applicationContext.getString(R.string.user_number_helps_zero)
                1 -> findViewById<TextView>(R.id.user_number_helps).text = applicationContext.getString(R.string.user_number_helps_singular, user.numberOfHelps.toString())
                else -> findViewById<TextView>(R.id.user_number_helps).text = applicationContext.getString(R.string.user_number_helps_plural, user.numberOfHelps.toString())
            }

        } else {
            findViewById<TextView>(R.id.user_help_types).text = getString(R.string.filter_supported_radio)
            findViewById<TextView>(R.id.user_number_helps).visibility = View.GONE
        }

        findViewById<TextView>(R.id.user_profession).text = user.profession

        val genderTextView = findViewById<TextView>(R.id.user_gender)
        when (user.gender) {
            Gender.FEMALE -> genderTextView.text = applicationContext.getString(R.string.filter_female_switch)
            Gender.MALE -> genderTextView.text = applicationContext.getString(R.string.filter_male_switch)
            Gender.BOTH -> genderTextView.visibility = View.GONE
        }

        findViewById<TextView>(R.id.user_email).text = user.email

        if (user.isPhoneAvailable) {
            findViewById<TextView>(R.id.user_phone).text = MaskEditUtil.mask(user.phone.removePrefix("55"), MaskEditUtil.PHONE_MASK)
        } else {
            findViewById<TextView>(R.id.user_phone).visibility = View.GONE
        }

        if (isHistoryView && match != null && mutableListOf(MatchStatus.ACCEPT, MatchStatus.DONE).contains(match!!.status)) {
            setVisibilityOfContactViews(View.VISIBLE)
        } else {
            setVisibilityOfContactViews(View.GONE)
        }
    }

    private fun configureButtons() {
        findViewById<ImageButton>(R.id.back_button).setOnClickListener { finish() }

        findViewById<ImageButton>(R.id.report_button).setOnClickListener {
            val intent = Intent(this, ReportUserActivity::class.java).putExtra(
                USER,
                JsonUtil.toJson(user)
            )
            startActivity(intent)
        }

        findViewById<Button>(R.id.user_send_invitation).setOnClickListener {
            sendInvite()
        }

        findViewById<RatingBar>(R.id.user_rating_bar).setOnRatingBarChangeListener { _, rating, _ ->
            findViewById<Button>(R.id.user_send_rating).isEnabled = rating > 0
        }

        findViewById<Button>(R.id.user_send_rating).setOnClickListener {
            rate()
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

    private fun rate() {
        findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        findViewById<Button>(R.id.user_send_rating).isEnabled = false

        userController.rate(user, findViewById<RatingBar>(R.id.user_rating_bar).rating, object : Callback<Double> {
            override fun onResponse(call: Call<Double>, response: Response<Double>) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE

                if (response.isSuccessful && response.body() != null && response.body() != 0.0) {
                    findViewById<TextView>(R.id.user_rating).text = applicationContext.getString(R.string.user_rating, response.body()!!.toString())
                    Toast.makeText(applicationContext, applicationContext.getString(R.string.user_rating_success_message), Toast.LENGTH_LONG).show()
                    findViewById<RatingBar>(R.id.user_rating_bar).setIsIndicator(true)
                } else {
                    findViewById<Button>(R.id.user_send_rating).isEnabled = true
                }
            }

            override fun onFailure(call: Call<Double>, t: Throwable) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                findViewById<Button>(R.id.user_send_rating).isEnabled = true
                //TODO: Show error message
            }
        })
    }

    private fun getRate() {
        findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE

        userController.getRate(user, object : Callback<Double> {
            override fun onResponse(call: Call<Double>, response: Response<Double>) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE

                if (response.isSuccessful && response.body() != null && response.body() != 0.0) {
                    findViewById<RatingBar>(R.id.user_rating_bar).rating = response.body()!!.toFloat()
                    findViewById<Button>(R.id.user_send_rating).isEnabled = false
                }
            }

            override fun onFailure(call: Call<Double>, t: Throwable) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
            }
        })
    }

    private fun setVisibilityOfContactViews(visibility: Int) {
        findViewById<TextView>(R.id.user_email).visibility = visibility
        findViewById<TextView>(R.id.user_phone).visibility = visibility
        findViewById<RatingBar>(R.id.user_rating_bar).visibility = visibility
        findViewById<Button>(R.id.user_send_rating).visibility = visibility
        findViewById<Button>(R.id.user_send_invitation).visibility =
            if (isHistoryView || isPendingView) View.GONE else View.VISIBLE
    }
}
