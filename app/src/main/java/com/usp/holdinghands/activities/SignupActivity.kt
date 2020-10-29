package com.usp.holdinghands.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.R
import com.usp.holdinghands.utils.MaskEditUtil

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setupButtons()
        setupMasks()
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

    private fun setupMasks() {
        val zipTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_zipcode)
        zipTextInputLayout.editText!!.addTextChangedListener(MaskEditUtil.mask(zipTextInputLayout.editText!!, MaskEditUtil.ZIP_MASK))

        val phoneTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_phone)
        phoneTextInputLayout.editText!!.addTextChangedListener(MaskEditUtil.mask(phoneTextInputLayout.editText!!, MaskEditUtil.PHONE_MASK))

        val birthDateTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_birth)
        birthDateTextInputLayout.editText!!.addTextChangedListener(MaskEditUtil.mask(birthDateTextInputLayout.editText!!, MaskEditUtil.DATE_MASK))
    }
}
