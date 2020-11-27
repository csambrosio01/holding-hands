package com.usp.holdinghands.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.usp.holdinghands.R
import com.usp.holdinghands.adapter.ViewPagerAdapter

class HelpFragment : Fragment() {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.addFragment(PendingFragment.newInstance(), activity!!.applicationContext.getString(R.string.pending_fragment_title))
        viewPagerAdapter.addFragment(HistoryFragment.newInstance(), activity!!.applicationContext.getString(R.string.history_fragment_title))

        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = viewPagerAdapter
        viewPager.isSaveEnabled = false

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getPageTitle(position)
        }.attach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HelpFragment()
    }
}
