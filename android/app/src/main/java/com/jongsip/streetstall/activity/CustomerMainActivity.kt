package com.jongsip.streetstall.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jongsip.streetstall.R
import com.jongsip.streetstall.fragment.*
import com.jongsip.streetstall.login.LoginActivity

class CustomerMainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_customer)

        bottomNavigation = findViewById(R.id.bottom_navi_customer)

        supportFragmentManager.beginTransaction().add(R.id.fragment_frame_customer, MapsFragment())
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener {
            replaceFragment(
                when (it.itemId) {
                    R.id.menu_map -> MapsFragment()
                    R.id.menu_search -> SearchFragment()
                    R.id.menu_bookmark -> BookmarkFragment()
                    else -> SettingFragment()
                }
            )
            true
        }
    }

    private fun replaceFragment(fragmentClass: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame_customer, (fragmentClass))
            .addToBackStack("adds").commit()
    }
}