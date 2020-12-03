package com.usp.holdinghands.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.R
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.LoginDTO
import com.usp.holdinghands.model.LoginResponse
import com.usp.holdinghands.utils.validators.EmailValidator
import com.usp.holdinghands.utils.validators.PasswordValidator
import com.usp.holdinghands.utils.validators.Validator
import com.usp.holdinghands.utils.validators.ValidatorActivity
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class LoginActivity : AppCompatActivity(), ValidatorActivity {

    override val validators = mutableListOf<Validator>()

    private lateinit var userController: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userController = UserController(applicationContext)

        setupButtons()
        setupValidators()
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.bt_sign_up).setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        super.setupMainButton(findViewById(R.id.bt_login))
    }

    override fun setupValidators() {
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

    override fun mainButtonClicked() {
        val login = makeLogin()

        findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        findViewById<Button>(R.id.bt_sign_up).isEnabled = false
        findViewById<Button>(R.id.bt_login).isEnabled = false
        findViewById<Button>(R.id.bt_forgot_password).isEnabled = false
        userController.login(login, object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                findViewById<Button>(R.id.bt_sign_up).isEnabled = true
                findViewById<Button>(R.id.bt_login).isEnabled = true
                findViewById<Button>(R.id.bt_forgot_password).isEnabled = true
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    userController.setLogin(loginResponse)

                    val intent = Intent(applicationContext, NavigationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                print(t.localizedMessage)
                // TODO: Show error message
            }
        })
    }

    private fun makeLogin(): LoginDTO {
        return LoginDTO(
            findViewById<TextInputLayout>(R.id.login_email).editText!!.text.toString(),
            findViewById<TextInputLayout>(R.id.login_password).editText!!.text.toString()
        )
    }
}
