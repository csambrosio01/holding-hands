package com.usp.holdinghands.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.R
import com.usp.holdinghands.activities.FILTERED_USERS
import com.usp.holdinghands.adapter.HelpAdapter
import com.usp.holdinghands.adapter.UserAdapter
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.User

class HelpFragment : Fragment() {

    private val users = mutableListOf<User>()
    private lateinit var userController: UserController

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userController = UserController(activity!!.applicationContext)

        configureRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == FILTER_ACTIVITY_REQUEST_CODE) {
            if (data != null && data.hasExtra(FILTERED_USERS)) {
                users.clear()
                users.addAll(userController.fromJsonString(data.extras!!.getString(FILTERED_USERS)!!))
                viewAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun configureRecyclerView() {
        users.addAll(userController.getUsers())

        viewManager = LinearLayoutManager(activity!!.applicationContext)
        viewAdapter = HelpAdapter(users, activity!!.applicationContext)

        recyclerView = view!!.findViewById<RecyclerView>(R.id.help_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HelpFragment()
    }
}
