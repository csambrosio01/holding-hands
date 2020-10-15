package com.usp.holdinghands

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.usp.holdinghands.controller.UserController

const val FILTERED_USERS = "filtered_users"

class FilterActivity : AppCompatActivity() {

    private lateinit var userController: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        userController = UserController(applicationContext)

        configureToolbar()
        configureAgeSliderLayout()
        configureDistanceSlidesLayout()
        configureNumberOfHelpsSliderLayout()
        configureAllCategoriesSwitch()
    }

    private fun configureToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun configureAgeSliderLayout() {
        findViewById<TextView>(R.id.age_slider_values).text =
            getString(R.string.range_slider_value, "18", "25")

        findViewById<RangeSlider>(R.id.age_slider).addOnChangeListener { slider: RangeSlider, _: Float, _: Boolean ->
            findViewById<TextView>(R.id.age_slider_values).text = getString(
                R.string.range_slider_value,
                slider.values[0].toInt().toString(),
                slider.values[1].toInt().toString()
            )
        }
    }

    private fun configureDistanceSlidesLayout() {
        findViewById<TextView>(R.id.distance_slider_value).text =
            getString(R.string.distance_slider_value, "30")

        findViewById<Slider>(R.id.distance_slider).addOnChangeListener { slider: Slider, _: Float, _: Boolean ->
            findViewById<TextView>(R.id.distance_slider_value).text = getString(
                R.string.distance_slider_value,
                slider.value.toInt().toString()
            )
        }
    }

    private fun configureNumberOfHelpsSliderLayout() {
        findViewById<TextView>(R.id.number_helps_slider_value).text =
            getString(R.string.range_slider_value, "0", "50")

        findViewById<RangeSlider>(R.id.number_helps_slider).addOnChangeListener { slider: RangeSlider, _: Float, _: Boolean ->
            findViewById<TextView>(R.id.number_helps_slider_value).text = getString(
                R.string.range_slider_value,
                slider.values[0].toInt().toString(),
                if (slider.values[1].toInt() == 100) "100+" else slider.values[1].toInt().toString()
            )
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.volunteer_radio ->
                    if (checked) {
                        findViewById<ConstraintLayout>(R.id.filter_number_helps).visibility =
                            View.VISIBLE
                    }
                R.id.supported_radio ->
                    if (checked) {
                        findViewById<ConstraintLayout>(R.id.filter_number_helps).visibility =
                            View.GONE
                    }
            }
        }
    }

    private fun configureAllCategoriesSwitch() {
        findViewById<SwitchCompat>(R.id.filter_category_6_switch).setOnCheckedChangeListener { view: CompoundButton?, isChecked: Boolean ->
            findViewById<SwitchCompat>(R.id.filter_category_1_switch).isChecked = isChecked
            findViewById<SwitchCompat>(R.id.filter_category_2_switch).isChecked = isChecked
            findViewById<SwitchCompat>(R.id.filter_category_3_switch).isChecked = isChecked
            findViewById<SwitchCompat>(R.id.filter_category_4_switch).isChecked = isChecked
            findViewById<SwitchCompat>(R.id.filter_category_5_switch).isChecked = isChecked
        }
    }
}
