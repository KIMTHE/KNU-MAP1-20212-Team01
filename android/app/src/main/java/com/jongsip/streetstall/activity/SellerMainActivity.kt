package com.jongsip.streetstall.activity

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jongsip.streetstall.R
import com.jongsip.streetstall.fragment.*

class SellerMainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    companion object {
        const val PERMISSION_CODE_ACCEPTED = 1
        const val PERMISSION_CODE_NOT_AVAILABLE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_seller)

        bottomNavigation = findViewById(R.id.bottom_navi_seller)

        supportFragmentManager.beginTransaction().add(R.id.fragment_frame_seller, MapsFragment())
            .commit()

        requestLocationPermission()//위치 권한 요청

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

    fun requestLocationPermission(): Int {//권한요청
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                // request permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    SellerMainActivity.PERMISSION_CODE_ACCEPTED)
            }
        } else {
            // already granted
            return SellerMainActivity.PERMISSION_CODE_ACCEPTED
        }
        // not available
        return SellerMainActivity.PERMISSION_CODE_NOT_AVAILABLE
    }
}