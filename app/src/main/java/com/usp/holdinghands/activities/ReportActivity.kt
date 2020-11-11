package com.usp.holdinghands.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.usp.holdinghands.R

class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        configureButton()
    }

    private fun configureButton() {
        findViewById<Button>(R.id.send_report).setOnClickListener {
            Toast.makeText(applicationContext, applicationContext.getString(R.string.report_sucessful_message), Toast.LENGTH_LONG).show()
        }
    }

}
