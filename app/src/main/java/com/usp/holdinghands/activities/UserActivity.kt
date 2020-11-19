package com.usp.holdinghands.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.usp.holdinghands.R
import com.usp.holdinghands.adapter.USER
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.Gender
import com.usp.holdinghands.model.User
import com.usp.holdinghands.model.getHelpAsString

class UserActivity : AppCompatActivity() {

    private lateinit var userController: UserController
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        userController = UserController(applicationContext)
        user = userController.fromJsonStringUser(intent.extras!!.getString(USER)!!)

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
    }
}
