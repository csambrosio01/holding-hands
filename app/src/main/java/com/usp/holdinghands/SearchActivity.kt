package com.usp.holdinghands

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.adapter.UserAdapter
import com.usp.holdinghands.helper.JsonParser
import com.usp.holdinghands.model.*

class SearchActivity : AppCompatActivity() {

    private val users = mutableListOf<User>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        readJson()

        viewManager = LinearLayoutManager(this)
        viewAdapter = UserAdapter(users)

        recyclerView = findViewById<RecyclerView>(R.id.search_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun readJson() {
        val jsonParser = JsonParser(applicationContext)
        val json = jsonParser.getJsonFromFile(R.raw.users_mock)

        val usersJson = json.getJSONArray(TAG_USERS)

        for (i in 0 until usersJson.length()) {
            val u = usersJson.getJSONObject(i)

            val helpTypesJson = u.getJSONArray(TAG_HELP_TYPES)

            val helpTypes = List(helpTypesJson.length()) {
                helpTypesJson.getString(it)
            }

            users.add(
                User(
                    u.getString(TAG_NAME),
                    u.getInt(TAG_AGE),
                    u.getString(TAG_DISTANCE),
                    helpTypes,
                    u.getString(TAG_IMAGE)
                )
            )
        }
    }
}
