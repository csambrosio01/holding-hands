package com.usp.holdinghands.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.R
import com.usp.holdinghands.utils.validators.EmailValidator
import com.usp.holdinghands.utils.validators.PasswordValidator
import com.usp.holdinghands.utils.validators.Validator

class LoginActivity : AppCompatActivity() {

    private val validators = mutableListOf<Validator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupButtons()
        setupValidators()
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.bt_login).setOnClickListener {
            if (validateFields()) {
                //TODO: Make login

                val intent = Intent(this, NavigationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                //TODO: Show error message?
            }
        }

        findViewById<Button>(R.id.bt_sign_up).setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupValidators() {
        val emailTextInputLayout = findViewById<TextInputLayout>(R.id.login_email)
        val emailValidator = EmailValidator(true, emailTextInputLayout)
        emailTextInputLayout.editText!!.onFocusChangeListener = emailValidator

        val passwordTextInputLayout = findViewById<TextInputLayout>(R.id.login_password)
        val passwordValidator = PasswordValidator(true, passwordTextInputLayout)
        passwordTextInputLayout.editText!!.onFocusChangeListener = passwordValidator

        validators.addAll(
            mutableListOf(
                emailValidator,
                passwordValidator
            )
        )
    }

    private fun validateFields(): Boolean {
        var isValid = true
        validators.forEach {
            it.validate()
            if (!it.isValid) isValid = false
        }
        return isValid
    }
}
