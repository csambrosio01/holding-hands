package com.usp.holdinghands.activities

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.LocationService
import com.usp.holdinghands.R
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.utils.MaskEditUtil
import com.usp.holdinghands.utils.validators.*

const val PERMISSION_GRANTED_REQUEST_CODE = 156

class SignupActivity : AppCompatActivity(), ValidatorActivity, LocationService {

    override val validators = mutableListOf<Validator>()

    private lateinit var userController: UserController

    override lateinit var fusedLocationClient: FusedLocationProviderClient
    override lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        userController = UserController(applicationContext)

        setupButtons()
        setupMasks()
        setupValidators()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        onRequestPermissionsResult(requestCode, grantResults)
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

        val professionTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_profession)
        val professionValidator = TextValidator(true, professionTextInputLayout)
        professionTextInputLayout.editText!!.onFocusChangeListener = professionValidator

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
                professionValidator,
                zipValidator,
                addressValidator,
                numberValidator,
                emailValidator,
                passwordValidator,
                confirmPasswordValidator
            )
        )
    }

    override fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_GRANTED_REQUEST_CODE
        )
        return
    }

    override fun onLocationResult(location: Location) {
        createUser()
    }

    override fun mainButtonClicked() {
        getLocation()
    }

    private fun createUser() {
        val intent = Intent(applicationContext, NavigationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
