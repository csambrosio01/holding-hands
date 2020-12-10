package com.usp.holdinghands.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.usp.holdinghands.R
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.Gender
import com.usp.holdinghands.model.HelpType
import com.usp.holdinghands.model.UserFilter
import com.usp.holdinghands.model.UserResponse
import com.usp.holdinghands.utils.EnumConverter
import com.usp.holdinghands.utils.JsonUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val USER_FILTER = "user_filter"

class FilterActivity : AppCompatActivity() {

    private lateinit var userController: UserController
    private lateinit var user: UserResponse

    private var userFilter: UserFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        userController = UserController(applicationContext)
        user = userController.getLoggedUser()!!

        if (intent.extras?.getString(USER_FILTER) != null) {
            userFilter = JsonUtil.fromJson(intent.extras!!.getString(USER_FILTER)!!)
        }

        configureToolbar()
        configureGenderLayout()
        configureAgeSliderLayout()
        configureDistanceSlidesLayout()
        configureSupportedExclusiveViews()
        configureFilterButton()
    }

    private fun configureToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun configureGenderLayout() {
        val maleSwitch = findViewById<SwitchCompat>(R.id.male_switch)
        val femaleSwitch = findViewById<SwitchCompat>(R.id.female_switch)

        if (userFilter != null) {
            when (userFilter!!.gender) {
                Gender.MALE -> {
                    maleSwitch.isChecked = true
                    femaleSwitch.isChecked = false
                }
                Gender.FEMALE -> {
                    maleSwitch.isChecked = false
                    femaleSwitch.isChecked = true
                }
                Gender.BOTH -> {
                    maleSwitch.isChecked = true
                    femaleSwitch.isChecked = true
                }
            }
        } else {
            maleSwitch.isChecked = true
            femaleSwitch.isChecked = true
        }
    }

    private fun configureAgeSliderLayout() {
        val ageSliderValues = findViewById<TextView>(R.id.age_slider_values)
        val ageSlider = findViewById<RangeSlider>(R.id.age_slider)

        if (userFilter != null) {
            ageSliderValues.text = getString(R.string.range_slider_value, userFilter!!.ageMin.toString(), userFilter!!.ageMax.toString())
            ageSlider.values = mutableListOf(userFilter!!.ageMin.toFloat(), userFilter!!.ageMax.toFloat())
        } else {
            ageSliderValues.text = getString(R.string.range_slider_value, "18", "25")
            ageSlider.values = mutableListOf(18F, 25F)
        }

        ageSlider.addOnChangeListener { slider: RangeSlider, _: Float, _: Boolean ->
            ageSliderValues.text = getString(
                R.string.range_slider_value,
                slider.values[0].toInt().toString(),
                slider.values[1].toInt().toString()
            )
        }
    }

    private fun configureDistanceSlidesLayout() {
        val distanceSliderValues = findViewById<TextView>(R.id.distance_slider_value)
        val distanceSlider = findViewById<Slider>(R.id.distance_slider)

        if (userFilter != null) {
            distanceSliderValues.text = getString(R.string.distance_slider_value, userFilter!!.distance.toString())
            distanceSlider.value = userFilter!!.distance.toFloat()
        } else {
            distanceSliderValues.text = getString(R.string.distance_slider_value, "30")
            distanceSlider.value = 30F
        }

        distanceSlider.addOnChangeListener { slider: Slider, _: Float, _: Boolean ->
            distanceSliderValues.text = getString(
                R.string.distance_slider_value,
                slider.value.toInt().toString()
            )
        }
    }

    private fun configureNumberOfHelpsSliderLayout() {
        val numberOfHelpsSliderValues = findViewById<TextView>(R.id.number_helps_slider_value)
        val numberOfHelpsSlider = findViewById<RangeSlider>(R.id.number_helps_slider)

        if (userFilter != null) {
            numberOfHelpsSliderValues.text = getString(R.string.range_slider_value, userFilter!!.helpNumberMin.toString(), userFilter!!.helpNumberMax.toString())
            numberOfHelpsSlider.values = mutableListOf(userFilter!!.helpNumberMin!!.toFloat(), userFilter!!.helpNumberMax!!.toFloat())
        } else {
            numberOfHelpsSliderValues.text = getString(R.string.range_slider_value, "0", "50")
            numberOfHelpsSlider.values = mutableListOf(0F, 50F)
        }

        numberOfHelpsSlider.addOnChangeListener { slider: RangeSlider, _: Float, _: Boolean ->
            numberOfHelpsSliderValues.text = getString(
                R.string.range_slider_value,
                slider.values[0].toInt().toString(),
                if (slider.values[1].toInt() == 100) "100+" else slider.values[1].toInt().toString()
            )
        }
    }

    private fun configureSupportedExclusiveViews() {
        if (user.isHelper) {
            findViewById<ConstraintLayout>(R.id.filter_number_helps).visibility =
                View.GONE
            findViewById<ConstraintLayout>(R.id.filter_categories).visibility =
                View.GONE
        } else {
            findViewById<ConstraintLayout>(R.id.filter_number_helps).visibility =
                View.VISIBLE
            findViewById<ConstraintLayout>(R.id.filter_categories).visibility =
                View.VISIBLE
            configureNumberOfHelpsSliderLayout()
            configureCategoriesLayout()
        }
    }

    private fun configureCategoriesLayout() {
        val helpTypeOneSwitch = findViewById<SwitchCompat>(R.id.filter_category_1_switch)
        val helpTypeTwoSwitch = findViewById<SwitchCompat>(R.id.filter_category_2_switch)
        val helpTypeThreeSwitch = findViewById<SwitchCompat>(R.id.filter_category_3_switch)
        val helpTypeFourSwitch = findViewById<SwitchCompat>(R.id.filter_category_4_switch)
        val helpTypeFiveSwitch = findViewById<SwitchCompat>(R.id.filter_category_5_switch)
        val helpTypeAllSwitch = findViewById<SwitchCompat>(R.id.filter_category_6_switch)

        if (userFilter != null) {
            val helpTypeList = userFilter!!.helpTypes

            helpTypeOneSwitch.isChecked = helpTypeList!!.contains(HelpType.TYPE_1)
            helpTypeTwoSwitch.isChecked = helpTypeList.contains(HelpType.TYPE_2)
            helpTypeThreeSwitch.isChecked = helpTypeList.contains(HelpType.TYPE_3)
            helpTypeFourSwitch.isChecked = helpTypeList.contains(HelpType.TYPE_4)
            helpTypeFiveSwitch.isChecked = helpTypeList.contains(HelpType.TYPE_5)
            helpTypeAllSwitch.isChecked = helpTypeList.size == 5
        }

        helpTypeAllSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            helpTypeOneSwitch.isChecked = isChecked
            helpTypeTwoSwitch.isChecked = isChecked
            helpTypeThreeSwitch.isChecked = isChecked
            helpTypeFourSwitch.isChecked = isChecked
            helpTypeFiveSwitch.isChecked = isChecked
        }
    }

    private fun configureFilterButton() {
        findViewById<Button>(R.id.filter_button).setOnClickListener {
            GlobalScope.launch {
                setResult(
                    Activity.RESULT_OK,
                    Intent().putExtra(
                        USER_FILTER,
                        JsonUtil.toJson(makeUserFilter())
                    )
                )
                finish()
            }
        }
    }

    private fun makeUserFilter(): UserFilter {
        //TODO: Fix gender when both are unchecked
        val gender: Gender = if (findViewById<SwitchCompat>(R.id.male_switch).isChecked) {
            if (findViewById<SwitchCompat>(R.id.female_switch).isChecked) {
                Gender.BOTH
            } else {
                Gender.MALE
            }
        } else {
            Gender.FEMALE
        }

        var helpTypes: MutableList<HelpType>? = null
        var helpNumberMin: Int? = null
        var helpNumberMax: Int? = null

        if (!user.isHelper) {
            //TODO: Fix helpTypes when everyone are unchecked
            helpTypes = mutableListOf()

            if (findViewById<SwitchCompat>(R.id.filter_category_1_switch).isChecked) helpTypes.add(
                HelpType.TYPE_1
            )
            if (findViewById<SwitchCompat>(R.id.filter_category_2_switch).isChecked) helpTypes.add(
                HelpType.TYPE_2
            )
            if (findViewById<SwitchCompat>(R.id.filter_category_3_switch).isChecked) helpTypes.add(
                HelpType.TYPE_3
            )
            if (findViewById<SwitchCompat>(R.id.filter_category_4_switch).isChecked) helpTypes.add(
                HelpType.TYPE_4
            )
            if (findViewById<SwitchCompat>(R.id.filter_category_5_switch).isChecked) helpTypes.add(
                HelpType.TYPE_5
            )

            val helpNumberSlider = findViewById<RangeSlider>(R.id.number_helps_slider)
            helpNumberMin = helpNumberSlider.values[0].toInt()
            helpNumberMax = helpNumberSlider.values[1].toInt()
        }

        return UserFilter(
            gender = gender,
            ageMin = findViewById<RangeSlider>(R.id.age_slider).values[0].toInt(),
            ageMax = findViewById<RangeSlider>(R.id.age_slider).values[1].toInt(),
            distance = findViewById<Slider>(R.id.distance_slider).value.toDouble(),
            helpTypes = helpTypes,
            helpNumberMin = helpNumberMin,
            helpNumberMax = helpNumberMax
        )
    }
}
