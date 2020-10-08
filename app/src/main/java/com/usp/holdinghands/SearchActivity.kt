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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        configureRecyclerView()
    }

    private fun configureRecyclerView() {
        readJson()

        findViewById<RecyclerView>(R.id.search_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this)
            adapter = UserAdapter(users)
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
