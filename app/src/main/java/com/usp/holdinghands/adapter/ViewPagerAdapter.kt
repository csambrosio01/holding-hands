package com.usp.holdinghands.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val mFragmentList = mutableListOf<Fragment>()
    private val mFragmentTitleList = mutableListOf<String>()

    override fun getItemCount(): Int = mFragmentList.size

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String = "") {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}
