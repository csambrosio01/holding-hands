package com.usp.holdinghands.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.usp.holdinghands.R

class ReportUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_user)

        configureViews()
    }

    private fun configureViews() {
        findViewById<RadioButton>(R.id.report_other).setOnCheckedChangeListener { _, isChecked ->
            val et = findViewById<EditText>(R.id.et_report_other)
            if (isChecked) {
                et.visibility = View.VISIBLE
                et.requestFocus()
            } else {
                et.visibility = View.GONE
            }
        }

        findViewById<Button>(R.id.send_report).setOnClickListener {
            //TODO: Make API request
            Toast.makeText(applicationContext, applicationContext.getString(R.string.report_sucessful_message), Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
