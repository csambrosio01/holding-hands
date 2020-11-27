package com.usp.holdinghands.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.usp.holdinghands.R
import com.usp.holdinghands.fragments.HelpFragment
import com.usp.holdinghands.fragments.HomeFragment
import com.usp.holdinghands.fragments.NotificationFragment
import com.usp.holdinghands.fragments.UserProfileFragment

class NavigationActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment.newInstance()
    private val helpFragment = HelpFragment.newInstance()
    private val notificationFragment = NotificationFragment.newInstance()
    private val userProfileFragment = UserProfileFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        replaceFragment(homeFragment)

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_home -> replaceFragment(homeFragment)
                R.id.bottom_help_request -> replaceFragment(helpFragment)
                R.id.bottom_profile -> replaceFragment(userProfileFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
