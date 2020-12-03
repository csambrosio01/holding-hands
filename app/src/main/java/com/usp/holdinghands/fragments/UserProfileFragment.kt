package com.usp.holdinghands.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.usp.holdinghands.R
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.LoggedUser
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class UserProfileFragment : Fragment() {

    private lateinit var user: LoggedUser
    private lateinit var userController: UserController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userController = UserController(activity!!.applicationContext)
        user = userController.getLoggedUser()!!

        setupViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    private fun setupViews() {
        val imageId = activity!!.applicationContext.resources.getIdentifier(
            "caio",
            "drawable",
            activity!!.applicationContext.packageName
        )

        view!!.findViewById<CircleImageView>(R.id.profile_image).setImageResource(imageId)
        view!!.findViewById<TextView>(R.id.profile_name).text = user.name
        view!!.findViewById<TextView>(R.id.profile_card_help_number).text = "15"
        view!!.findViewById<TextView>(R.id.profile_email_info).text = user.email
        view!!.findViewById<TextView>(R.id.profile_phone_info).text = user.phone
        view!!.findViewById<TextView>(R.id.profile_birth_info).text = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(user.birth)
        view!!.findViewById<TextView>(R.id.profile_profession_info).text = user.profession
        view!!.findViewById<TextView>(R.id.profile_user_help_types).text = user.helpTypes

        view!!.findViewById<SwitchCompat>(R.id.profile_card_volunteer_switch).setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                view!!.findViewById<ConstraintLayout>(R.id.helper_section).visibility = View.VISIBLE
            } else {
                view!!.findViewById<ConstraintLayout>(R.id.helper_section).visibility = View.GONE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserProfileFragment()
    }
}
