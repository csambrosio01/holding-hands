package com.usp.holdinghands.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.usp.holdinghands.R
import com.usp.holdinghands.adapter.HelpAdapter
import com.usp.holdinghands.adapter.NotificationAdapter
import com.usp.holdinghands.model.Notification

class NotificationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configureRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    //TODO: remove this function that is only for a macro view of notifications screen
    private fun generateNotificationsList(size: Int): MutableList<Notification> {

        val list = mutableListOf<Notification>()

        for(i in 0 until size) {
            val notification = Notification(R.drawable.ic_notification, getString(R.string.notification_default), getString(R.string.notification_date))
            list += notification
        }

        return list
    }

    private fun configureRecyclerView() {
        val notifications = generateNotificationsList(20)

        viewManager = LinearLayoutManager(activity!!.applicationContext)
        viewAdapter = NotificationAdapter(notifications)

        recyclerView = view!!.findViewById<RecyclerView>(R.id.notifications_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NotificationFragment()
    }
}
