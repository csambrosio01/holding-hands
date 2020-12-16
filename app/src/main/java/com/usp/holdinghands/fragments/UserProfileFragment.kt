package com.usp.holdinghands.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.usp.holdinghands.R
import com.usp.holdinghands.activities.LoginActivity
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.Gender
import com.usp.holdinghands.model.UserResponse
import com.usp.holdinghands.utils.EnumConverter
import com.usp.holdinghands.utils.MaskEditUtil
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class UserProfileFragment : Fragment() {

    private lateinit var user: UserResponse
    private lateinit var userController: UserController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userController = UserController(activity!!.applicationContext)
        user = userController.getLoggedUser()!!

        setupViews()
        setupButtons()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    private fun setupViews() {
        val defaultImageId = if (user.gender == Gender.MALE) {
            if (user.isHelper) {
                "young_man"
            } else {
                "old_man"
            }
        } else {
            if (user.isHelper) {
                "young_woman"
            } else {
                "old_woman"
            }
        }

        val imageId = activity!!.applicationContext.resources.getIdentifier(
            user.imageId ?: defaultImageId,
            "drawable",
            activity!!.applicationContext.packageName
        )

        view!!.findViewById<CircleImageView>(R.id.profile_image).setImageResource(imageId)
        view!!.findViewById<TextView>(R.id.profile_name).text = user.name
        view!!.findViewById<TextView>(R.id.profile_card_help_number).text = user.numberOfHelps.toString()
        view!!.findViewById<TextView>(R.id.profile_email_info).text = user.email
        view!!.findViewById<TextView>(R.id.profile_phone_info).text = MaskEditUtil.mask(user.phone.removePrefix("55"), MaskEditUtil.PHONE_MASK)
        view!!.findViewById<TextView>(R.id.profile_birth_info).text =
            SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(user.birth)
        view!!.findViewById<TextView>(R.id.profile_profession_info).text = user.profession
        view!!.findViewById<TextView>(R.id.profile_user_help_types).text = EnumConverter.getHelpAsString(EnumConverter.stringToEnumList(user.helpTypes))

        val volunteerSwitch = view!!.findViewById<SwitchCompat>(R.id.profile_card_volunteer_switch)
        volunteerSwitch
            .setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                val helperSection = view!!.findViewById<ConstraintLayout>(R.id.helper_section)
                val profileCard = view!!.findViewById<ConstraintLayout>(R.id.profile_card)
                if (isChecked) {
                    helperSection.visibility = View.VISIBLE
                    profileCard.visibility = View.VISIBLE
                } else {
                    helperSection.visibility = View.GONE
                    profileCard.visibility = View.GONE
                }
            }

        volunteerSwitch.setOnClickListener {
            updateIsHelper()
        }

        volunteerSwitch.isChecked = user.isHelper

        val phoneSwitch = view!!.findViewById<SwitchCompat>(R.id.profile_card_phone_switch)
        phoneSwitch.setOnClickListener {
            updateIsPhoneAvailable()
        }

        phoneSwitch.isChecked = user.isPhoneAvailable
    }

    private fun setupButtons() {
        view!!.findViewById<Button>(R.id.profile_logout_button).setOnClickListener {
            userController.logout()

            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun updateIsHelper() {
        view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        view!!.findViewById<Button>(R.id.profile_logout_button).isEnabled = false

        userController.updateIsHelper(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                view!!.findViewById<Button>(R.id.profile_logout_button).isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    user = response.body()!!
                } else {
                    Toast.makeText(activity!!.applicationContext, activity!!.applicationContext.getString(R.string.update_error), Toast.LENGTH_LONG).show()
                }

                view!!.findViewById<SwitchCompat>(R.id.profile_card_volunteer_switch).isChecked = user.isHelper
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                view!!.findViewById<Button>(R.id.profile_logout_button).isEnabled = true
                //TODO: Show error message
            }
        })
    }

    private fun updateIsPhoneAvailable() {
        view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        view!!.findViewById<Button>(R.id.profile_logout_button).isEnabled = false

        userController.updateIsPhoneAvailable(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                view!!.findViewById<Button>(R.id.profile_logout_button).isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    user = response.body()!!
                } else {
                    Toast.makeText(activity!!.applicationContext, activity!!.applicationContext.getString(R.string.update_error), Toast.LENGTH_LONG).show()
                }

                view!!.findViewById<SwitchCompat>(R.id.profile_card_phone_switch).isChecked = user.isPhoneAvailable
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                view!!.findViewById<Button>(R.id.profile_logout_button).isEnabled = true
                //TODO: Show error message
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserProfileFragment()
    }
}
