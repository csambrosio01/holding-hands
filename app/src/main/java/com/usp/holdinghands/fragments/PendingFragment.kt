package com.usp.holdinghands.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.usp.holdinghands.R
import com.usp.holdinghands.adapter.MatchAdapter
import com.usp.holdinghands.adapter.OnItemClickListener
import com.usp.holdinghands.controller.MatchController
import com.usp.holdinghands.model.MatchResponse
import com.usp.holdinghands.model.MatchStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingFragment : Fragment(), OnItemClickListener {

    private val matchs = mutableListOf<MatchResponse>()
    private lateinit var matchController: MatchController

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        matchController = MatchController(activity!!.applicationContext)

        configureRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onResume() {
        super.onResume()
        getMatchs()
    }

    private fun configureRecyclerView() {
        viewManager = LinearLayoutManager(activity!!.applicationContext)
        viewAdapter = MatchAdapter(matchs, activity!!.applicationContext, isPendingView = true, listener = this)

        recyclerView = view!!.findViewById<RecyclerView>(R.id.help_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        view!!.findViewById<SwipeRefreshLayout>(R.id.swipe_container).setOnRefreshListener {
            getMatchs()
        }
    }

    private fun getMatchs() {
        view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        view!!.findViewById<RecyclerView>(R.id.help_recycler_view).visibility = View.GONE

        matchController.getMatchs(MatchStatus.PENDING, object : Callback<List<MatchResponse>> {
            override fun onResponse(
                call: Call<List<MatchResponse>>,
                response: Response<List<MatchResponse>>
            ) {
                view!!.findViewById<SwipeRefreshLayout>(R.id.swipe_container).isRefreshing = false
                view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility =
                    View.GONE
                view!!.findViewById<RecyclerView>(R.id.help_recycler_view).visibility =
                    View.VISIBLE
                if (response.isSuccessful && response.body() != null) {
                    val matchList = response.body()!!
                    matchs.clear()
                    matchs.addAll(matchList)
                } else {
                    //TODO: Show error message
                }

                notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<MatchResponse>>, t: Throwable) {
                view!!.findViewById<SwipeRefreshLayout>(R.id.swipe_container).isRefreshing = false
                view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.GONE
                view!!.findViewById<RecyclerView>(R.id.help_recycler_view).visibility = View.VISIBLE
                notifyDataSetChanged()
                // TODO: Show error message
            }
        })
    }

    private fun notifyDataSetChanged() {
        val emptyView = view!!.findViewById<TextView>(R.id.empty_view)
        if (matchs.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            viewAdapter.notifyDataSetChanged()
        }
    }

    override fun onAccept(position: Int) {
        acceptRejectMatch(MatchStatus.ACCEPT, position)
    }

    override fun onDeny(position: Int) {
        acceptRejectMatch(MatchStatus.REJECT, position)
    }

    private fun acceptRejectMatch(matchStatus: MatchStatus, position: Int) {
        view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility = View.VISIBLE
        view!!.findViewById<View>(R.id.progress_view).visibility = View.VISIBLE

        matchController.acceptRejectMatch(
            matchStatus,
            matchs[position].matchId,
            object : Callback<MatchResponse> {
                override fun onResponse(
                    call: Call<MatchResponse>,
                    response: Response<MatchResponse>
                ) {
                    view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility =
                        View.GONE
                    view!!.findViewById<View>(R.id.progress_view).visibility = View.GONE

                    if (response.isSuccessful) {
                        matchs.removeAt(position)
                        Toast.makeText(
                            activity!!.applicationContext,
                            if (matchStatus == MatchStatus.ACCEPT) activity!!.applicationContext.getString(
                                R.string.help_accepted
                            ) else activity!!.applicationContext.getString(R.string.help_denied),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        //TODO: Show error message
                    }
                    notifyDataSetChanged()
                }

                override fun onFailure(call: Call<MatchResponse>, t: Throwable) {
                    view!!.findViewById<ConstraintLayout>(R.id.progress_layout).visibility =
                        View.GONE
                    view!!.findViewById<View>(R.id.progress_view).visibility = View.GONE
                    //TODO: Show error message
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance() = PendingFragment()
    }
}
