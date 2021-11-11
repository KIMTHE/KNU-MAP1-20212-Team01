package com.jongsip.streetstall.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jongsip.streetstall.R
import com.jongsip.streetstall.fragment.*

class SellerMainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_seller)

        bottomNavigation = findViewById(R.id.bottom_navi_seller)

        supportFragmentManager.beginTransaction().add(R.id.fragment_frame_seller, MapsFragment())
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener {
            replaceFragment(
                when (it.itemId) {
                    R.id.menu_map -> MapsFragment()
                    R.id.menu_search -> SearchFragment()
                    R.id.menu_manage -> ManageFragment()
                    else -> SettingFragment()
                }
            )
            true
        }
    }

    private fun replaceFragment(fragmentClass: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame_seller, (fragmentClass))
            .addToBackStack("adds").commit()
    }
}