package com.usp.holdinghands.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.FILTERED_USERS
import com.usp.holdinghands.FilterActivity
import com.usp.holdinghands.R
import com.usp.holdinghands.adapter.UserAdapter
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.User

const val FILTER_ACTIVITY_REQUEST_CODE = 10544

class HomeFragment : Fragment() {

    private val users = mutableListOf<User>()
    private lateinit var userController: UserController

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userController = UserController(activity!!.applicationContext)

        configureRecyclerView()
        configureFilterButton()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
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
        viewAdapter = UserAdapter(users, activity!!.applicationContext)

        recyclerView = view!!.findViewById<RecyclerView>(R.id.search_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun configureFilterButton() {
        view!!.findViewById<ImageButton>(R.id.search_filter).setOnClickListener {
            val intent = Intent(activity!!, FilterActivity::class.java)
            startActivityForResult(intent, FILTER_ACTIVITY_REQUEST_CODE)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
