package com.usp.holdinghands.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.usp.holdinghands.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupButtons()
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.bt_login).setOnClickListener {
            //TODO: Make login

            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
