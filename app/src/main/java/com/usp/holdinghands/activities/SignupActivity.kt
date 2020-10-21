package com.usp.holdinghands.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.usp.holdinghands.R

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setupButtons()
    }

    private fun setupButtons() {
        findViewById<ImageButton>(R.id.back_button).setOnClickListener { finish() }

        findViewById<Button>(R.id.sign_up_button).setOnClickListener {
            //TODO: Make signup

            val intent = Intent(applicationContext, NavigationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}
