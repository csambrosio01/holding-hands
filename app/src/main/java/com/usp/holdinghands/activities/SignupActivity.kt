package com.usp.holdinghands.activities

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputLayout
import com.usp.holdinghands.LocationService
import com.usp.holdinghands.R
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.Gender
import com.usp.holdinghands.model.HelpType
import com.usp.holdinghands.model.LoginResponse
import com.usp.holdinghands.model.UserDTO
import com.usp.holdinghands.utils.MaskEditUtil
import com.usp.holdinghands.utils.validators.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        setupViews()
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

    private fun setupViews() {
        findViewById<SwitchCompat>(R.id.sign_up_is_volunteer_switch).setOnCheckedChangeListener { _, isChecked ->
            findViewById<ConstraintLayout>(R.id.sign_up_help_types).visibility =
                if (isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun setupMasks() {
        val phoneTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_phone)
        phoneTextInputLayout.editText!!.addTextChangedListener(
            MaskEditUtil.mask(
                phoneTextInputLayout.editText!!,
                MaskEditUtil.PHONE_MASK
            )
        )

        val birthDateTextInputLayout = findViewById<TextInputLayout>(R.id.sign_up_birth)
        birthDateTextInputLayout.editText!!.addTextChangedListener(
            MaskEditUtil.mask(
                birthDateTextInputLayout.editText!!,
                MaskEditUtil.DATE_MASK
            )
        )
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
        createUser(location)
    }

    override fun mainButtonClicked() {
        getLocation()
    }

    private fun makeUser(location: Location): UserDTO {
        val checkedRadioButtonId =
            findViewById<RadioGroup>(R.id.sign_up_gender).checkedRadioButtonId

        val helpTypes = mutableListOf<HelpType>()

        if (findViewById<SwitchCompat>(R.id.help_type__1_switch).isChecked) helpTypes.add(
            HelpType.TYPE_1
        )
        if (findViewById<SwitchCompat>(R.id.help_type__2_switch).isChecked) helpTypes.add(
            HelpType.TYPE_2
        )
        if (findViewById<SwitchCompat>(R.id.help_type__3_switch).isChecked) helpTypes.add(
            HelpType.TYPE_3
        )
        if (findViewById<SwitchCompat>(R.id.help_type__4_switch).isChecked) helpTypes.add(
            HelpType.TYPE_4
        )
        if (findViewById<SwitchCompat>(R.id.help_type__5_switch).isChecked) helpTypes.add(
            HelpType.TYPE_5
        )

        if (helpTypes.isEmpty() && findViewById<SwitchCompat>(R.id.sign_up_is_volunteer_switch).isChecked) {
            //TODO: show error message
        }

        return UserDTO(
            name = findViewById<TextInputLayout>(R.id.sign_up_name).editText!!.text.toString(),
            helpTypes = helpTypes,
            gender = if (findViewById<RadioButton>(checkedRadioButtonId) == findViewById(R.id.sign_up_male)) Gender.MALE else Gender.FEMALE,
            profession = findViewById<TextInputLayout>(R.id.sign_up_profession).editText!!.text.toString(),
            birth = getBirthDate(findViewById<TextInputLayout>(R.id.sign_up_birth).editText!!.text.toString()),
            email = findViewById<TextInputLayout>(R.id.sign_up_email).editText!!.text.toString(),
            phone = MaskEditUtil.unmask("55" + findViewById<TextInputLayout>(R.id.sign_up_phone).editText!!.text.toString()),
            password = findViewById<TextInputLayout>(R.id.sign_up_password).editText!!.text.toString(),
            isHelper = findViewById<SwitchCompat>(R.id.sign_up_is_volunteer_switch).isChecked,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    private fun getBirthDate(input: String): String {
        val inputList = input.split("/")
        val stringBuilder = StringBuilder(inputList[2])

        stringBuilder.append("-")
        stringBuilder.append(inputList[1])
        stringBuilder.append("-")
        stringBuilder.append(inputList[0])

        return stringBuilder.toString()
    }

    private fun createUser(location: Location) {
        val user = makeUser(location)

        findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        findViewById<Button>(R.id.sign_up_button).isEnabled = false
        userController.createUser(user, object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                findViewById<Button>(R.id.sign_up_button).isEnabled = true
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    userController.setLogin(loginResponse)

                    val intent = Intent(applicationContext, NavigationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    //TODO: Show error message
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                findViewById<Button>(R.id.sign_up_button).isEnabled = true
                // TODO: Show error message
            }
        })
    }
}
