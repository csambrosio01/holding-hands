package com.usp.holdinghands.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.usp.holdinghands.R
import com.usp.holdinghands.activities.EditProfileActivity
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.User
import com.usp.holdinghands.model.getHelpAsString
import de.hdodenhof.circleimageview.CircleImageView

class UserProfileFragment : Fragment() {

    private lateinit var user: User
    private lateinit var userController: UserController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userController = UserController(activity!!.applicationContext)
        user = userController.getLoggedUser()

        setupViews()
        configureButton()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    private fun setupViews() {
        val imageId = activity!!.applicationContext.resources.getIdentifier(
            user.image,
            "drawable",
            activity!!.applicationContext.packageName
        )

        view!!.findViewById<CircleImageView>(R.id.profile_image).setImageResource(imageId)
        view!!.findViewById<TextView>(R.id.profile_name).text = user.name
        view!!.findViewById<TextView>(R.id.profile_card_help_number).text = user.numberOfHelps.toString()
        view!!.findViewById<TextView>(R.id.profile_email_info).text = user.email
        view!!.findViewById<TextView>(R.id.profile_phone_info).text = user.phone
        view!!.findViewById<TextView>(R.id.profile_birth_info).text = user.birth
        view!!.findViewById<TextView>(R.id.profile_profession_info).text = user.profession
        view!!.findViewById<TextView>(R.id.profile_address_info).text = user.address
        view!!.findViewById<TextView>(R.id.profile_user_help_types).text = user.getHelpAsString(false)
    }

    private fun configureButton() {
        view!!.findViewById<ImageButton>(R.id.profile_edit_button).setOnClickListener {
            val intent = Intent(activity!!, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserProfileFragment()
    }
}
