package com.usp.holdinghands.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.usp.holdinghands.LocationService
import com.usp.holdinghands.R
import com.usp.holdinghands.activities.FILTERED_USERS
import com.usp.holdinghands.activities.FilterActivity
import com.usp.holdinghands.activities.PERMISSION_GRANTED_REQUEST_CODE
import com.usp.holdinghands.adapter.UserAdapter
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.UserResponse
import com.usp.holdinghands.utils.JsonUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val FILTER_ACTIVITY_REQUEST_CODE = 10544

class HomeFragment : Fragment(), LocationService {

    private val users = mutableListOf<UserResponse>()

    private lateinit var userController: UserController

    override lateinit var fusedLocationClient: FusedLocationProviderClient
    override lateinit var locationCallback: LocationCallback

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
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
                users.addAll(JsonUtil.fromJson(data.extras!!.getString(FILTERED_USERS)!!))
                notifyDataSetChanged()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun requestPermissions() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_GRANTED_REQUEST_CODE)
    }

    private fun configureRecyclerView() {
        viewManager = LinearLayoutManager(activity!!.applicationContext)
        viewAdapter = UserAdapter(users, activity!!.applicationContext)

        recyclerView = view!!.findViewById<RecyclerView>(R.id.search_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        view!!.findViewById<SwipeRefreshLayout>(R.id.swipe_container).setOnRefreshListener {
            getLocation()
        }

        getLocation()
    }

    override fun onLocationResult(location: Location) {
        fetchUsers(location)
    }

    private fun fetchUsers(location: Location) {
        view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        view!!.findViewById<RecyclerView>(R.id.search_recycler_view).visibility = View.GONE

        userController.getUsers(location, object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                view!!.findViewById<SwipeRefreshLayout>(R.id.swipe_container).isRefreshing = false
                if (response.isSuccessful && response.body() != null) {
                    val usersList = response.body()!!
                    users.clear()
                    users.addAll(usersList)
                    view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                    view!!.findViewById<RecyclerView>(R.id.search_recycler_view).visibility = View.VISIBLE
                } else {
                    //TODO: Show error message
                }

                notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                view!!.findViewById<SwipeRefreshLayout>(R.id.swipe_container).isRefreshing = false
                view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                view!!.findViewById<RecyclerView>(R.id.search_recycler_view).visibility = View.VISIBLE
                notifyDataSetChanged()
                // TODO: Show error message
            }
        })
    }

    private fun configureFilterButton() {
        view!!.findViewById<ImageButton>(R.id.search_filter).setOnClickListener {
            val intent = Intent(activity!!, FilterActivity::class.java)
            startActivityForResult(intent, FILTER_ACTIVITY_REQUEST_CODE)
        }
    }

    private fun notifyDataSetChanged() {
        val emptyView = view!!.findViewById<TextView>(R.id.empty_view)
        if (users.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            viewAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
