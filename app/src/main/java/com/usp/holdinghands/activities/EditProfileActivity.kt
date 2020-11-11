package com.usp.holdinghands.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.usp.holdinghands.R

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        configureButtons()
    }

    private fun configureButtons() {
        findViewById<ImageButton>(R.id.edt_back_button).setOnClickListener{ finish() }

        findViewById<Button>(R.id.edt_save_button).setOnClickListener{
            Toast.makeText(applicationContext, applicationContext.getString(R.string.edit_save_success_message), Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
