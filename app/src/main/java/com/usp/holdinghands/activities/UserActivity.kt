package com.usp.holdinghands.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.usp.holdinghands.R
import com.usp.holdinghands.adapter.IS_HELP_VIEW
import com.usp.holdinghands.adapter.USER
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.Gender
import com.usp.holdinghands.model.User
import com.usp.holdinghands.model.getHelpAsString
import com.usp.holdinghands.utils.MaskEditUtil

class UserActivity : AppCompatActivity() {

    private lateinit var userController: UserController
    private lateinit var user: User
    private var isHelpView: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        userController = UserController(applicationContext)
        user = userController.fromJsonStringUser(intent.extras!!.getString(USER)!!)
        isHelpView = intent.extras!!.getBoolean(IS_HELP_VIEW)

        configureViews()
        configureButtons()
    }

    private fun configureViews() {
        val imageId = applicationContext.resources.getIdentifier(
            user.image,
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
        findViewById<TextView>(R.id.user_help_types).text = user.getHelpAsString()
        findViewById<TextView>(R.id.user_profession).text = user.profession

        when (user.gender) {
            Gender.FEMALE -> findViewById<TextView>(R.id.user_gender).text = applicationContext.getString(R.string.filter_female_switch)
            Gender.MALE -> findViewById<TextView>(R.id.user_gender).text = applicationContext.getString(R.string.filter_male_switch)
            Gender.BOTH -> { findViewById<TextView>(R.id.user_gender).visibility = View.GONE }
        }

        if (user.numberOfHelps <= 1) {
            findViewById<TextView>(R.id.user_number_helps).text = applicationContext.getString(R.string.user_number_helps_singular, user.numberOfHelps.toString())
        } else {
            findViewById<TextView>(R.id.user_number_helps).text = applicationContext.getString(R.string.user_number_helps_plural, user.numberOfHelps.toString())
        }

        findViewById<TextView>(R.id.user_email).text = user.email
        findViewById<TextView>(R.id.user_phone).text = MaskEditUtil.mask(user.phone ?: "", MaskEditUtil.PHONE_MASK)

        if (isHelpView) {
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
            Toast.makeText(applicationContext, applicationContext.getString(R.string.send_invitation_sucessful_message), Toast.LENGTH_LONG).show()
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

    private fun setVisibilityOfContactViews(visibility: Int) {
        findViewById<TextView>(R.id.user_email).visibility = visibility
        findViewById<TextView>(R.id.user_phone).visibility = visibility
        findViewById<RatingBar>(R.id.user_rating_bar).visibility = visibility
        findViewById<Button>(R.id.user_send_rating).visibility = visibility
        findViewById<Button>(R.id.user_send_invitation).visibility =
            if (visibility == View.GONE) View.VISIBLE else View.GONE
    }
}
