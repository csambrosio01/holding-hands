package com.usp.holdinghands.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.usp.holdinghands.R
import com.usp.holdinghands.controller.UserController

class MainActivity : AppCompatActivity() {

    private lateinit var userController: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userController = UserController(applicationContext)

        launcherAnimation()
    }

    private fun launcherAnimation() {
        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        val image = findViewById<ImageView>(R.id.holding_hands_logo)
        val text = findViewById<TextView>(R.id.holding_hands_text)

        image.startAnimation(topAnim)
        text.startAnimation(bottomAnim)

        Handler().postDelayed({
            sendToActivity()
        }, 2000)
    }

    private fun sendToActivity() {
        val intent = if (userController.getLoggedUser() != null) {
            Intent(this, NavigationActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
