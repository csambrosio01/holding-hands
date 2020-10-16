package com.usp.holdinghands

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.adapter.UserAdapter
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.User

const val FILTER_ACTIVITY_REQUEST_CODE = 10544

class SearchActivity : AppCompatActivity() {

    private val users = mutableListOf<User>()
    private lateinit var userController: UserController

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        userController = UserController(applicationContext)

        configureRecyclerView()
        configureFilterButton()
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

        viewManager = LinearLayoutManager(this)
        viewAdapter = UserAdapter(users)

        recyclerView = findViewById<RecyclerView>(R.id.search_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun configureFilterButton() {
        findViewById<ImageButton>(R.id.search_filter).setOnClickListener {
            val intent = Intent(this, FilterActivity::class.java)
            startActivityForResult(intent, FILTER_ACTIVITY_REQUEST_CODE)
        }
    }
}
