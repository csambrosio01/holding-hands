package com.usp.holdinghands

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.adapter.UserAdapter
import com.usp.holdinghands.controller.UserController
import com.usp.holdinghands.model.User

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
}
