package com.usp.holdinghands.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.usp.holdinghands.R
import com.usp.holdinghands.adapter.USER
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.ReportResponse
import com.usp.holdinghands.model.UserResponse
import com.usp.holdinghands.utils.JsonUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportUserActivity : AppCompatActivity() {

    private lateinit var userController: UserController
    private lateinit var user: UserResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_user)

        userController = UserController(this)
        user = JsonUtil.fromJson(intent.extras!!.getString(USER)!!)

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
            report()
        }
    }

    private fun report() {
        findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        findViewById<Button>(R.id.send_report).isEnabled = false

        val checkedRadioButtonId = findViewById<RadioGroup>(R.id.report_radio_group).checkedRadioButtonId
        val message =  findViewById<RadioButton>(checkedRadioButtonId).text.toString()

        userController.report(user, message, object : Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                findViewById<Button>(R.id.send_report).isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(applicationContext, applicationContext.getString(R.string.report_sucessful_message), Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    //TODO: show error message
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                findViewById<Button>(R.id.send_report).isEnabled = true
                //TODO: show error message
            }
        })
    }
}
