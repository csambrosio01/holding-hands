package com.usp.holdinghands.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.R
import com.usp.holdinghands.utils.MaskEditUtil
import com.usp.holdinghands.utils.validators.*

class SignupActivity : AppCompatActivity(), ValidatorActivity {

    override val validators = mutableListOf<Validator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setupButtons()
        setupMasks()
        setupValidators()
    }

    private fun setupButtons() {
        findViewById<ImageButton>(R.id.back_button).setOnClickListener { finish() }

        super.setupMainButton(findViewById(R.id.sign_up_button))
    }

    private fun setupMasks() {
        val zipTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_zipcode)
        zipTextInputLayout.editText!!.addTextChangedListener(MaskEditUtil.mask(zipTextInputLayout.editText!!, MaskEditUtil.ZIP_MASK))

        val phoneTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_phone)
        phoneTextInputLayout.editText!!.addTextChangedListener(MaskEditUtil.mask(phoneTextInputLayout.editText!!, MaskEditUtil.PHONE_MASK))

        val birthDateTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_birth)
        birthDateTextInputLayout.editText!!.addTextChangedListener(MaskEditUtil.mask(birthDateTextInputLayout.editText!!, MaskEditUtil.DATE_MASK))
    }

    override fun setupValidators() {
        val nameTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_name)
        val nameValidator = NameValidator(true, nameTextInputLayout)
        nameTextInputLayout.editText!!.onFocusChangeListener = nameValidator

        val phoneTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_phone)
        val phoneValidator = TextValidator(true, phoneTextInputLayout, exactLength = 15)
        phoneTextInputLayout.editText!!.onFocusChangeListener = phoneValidator

        val dateTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_birth)
        val dateValidator = DateValidator(true, dateTextInputLayout)
        dateTextInputLayout.editText!!.onFocusChangeListener = dateValidator

        val zipTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_zipcode)
        val zipValidator = TextValidator(true, zipTextInputLayout, exactLength = 9)
        zipTextInputLayout.editText!!.onFocusChangeListener = zipValidator

        val addressTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_address)
        val addressValidator = TextValidator(true, addressTextInputLayout)
        addressTextInputLayout.editText!!.onFocusChangeListener = addressValidator

        val numberTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_address_number)
        val numberValidator = TextValidator(true, numberTextInputLayout)
        numberTextInputLayout.editText!!.onFocusChangeListener = numberValidator

        val emailTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_email)
        val emailValidator = EmailValidator(true, emailTextInputLayout)
        emailTextInputLayout.editText!!.onFocusChangeListener = emailValidator

        val passwordTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_password)
        val passwordValidator = PasswordValidator(true, passwordTextInputLayout)
        passwordTextInputLayout.editText!!.onFocusChangeListener = passwordValidator

        val confirmPasswordTextInputLayout =
            findViewById<TextInputLayout>(R.id.sign_up_confirm_password)
        val confirmPasswordValidator =
            PasswordValidator(true, confirmPasswordTextInputLayout, passwordTextInputLayout)
        confirmPasswordTextInputLayout.editText!!.onFocusChangeListener = confirmPasswordValidator

        validators.addAll(
            mutableListOf(
                nameValidator,
                phoneValidator,
                dateValidator,
                zipValidator,
                addressValidator,
                numberValidator,
                emailValidator,
                passwordValidator,
                confirmPasswordValidator
            )
        )
    }

    override fun mainButtonClicked() {
        val intent = Intent(applicationContext, NavigationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
