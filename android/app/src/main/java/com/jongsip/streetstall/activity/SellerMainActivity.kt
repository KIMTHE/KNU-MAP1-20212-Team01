package com.jongsip.streetstall.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.jongsip.streetstall.R
import com.jongsip.streetstall.fragment.*
import com.jongsip.streetstall.util.PermissionUtil
import kotlin.system.exitProcess

class SellerMainActivity : AppCompatActivity(), MapsFragment.OnDataPassListener {

    private lateinit var bottomNavigation: BottomNavigationView
    lateinit var uid: String
    var lat = 0.0
    var lng = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_seller)

        uid = intent.getStringExtra("uid")!!
        bottomNavigation = findViewById(R.id.bottom_navi_seller)

        supportFragmentManager.beginTransaction().add(R.id.fragment_frame_seller, MapsFragment())
            .commit()

        replaceFragment(MapsFragment(),"map")
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_map -> replaceFragment(MapsFragment(),"map")
                R.id.menu_search -> replaceFragment(SearchFragment(),"search")
                R.id.menu_manage -> replaceFragment(ManageFragment(),"manage")
                else -> replaceFragment(SettingFragment(),"setting")
            }
            true
        }

        PermissionUtil.requestLocationPermission(this)//위치 권한 요청
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun replaceFragment(fragmentClass: Fragment, tag: String) {
        val bundle = Bundle()
        bundle.putString("uid", uid)
        bundle.putDouble("latitude", lat)
        bundle.putDouble("longitude", lng)
        fragmentClass.arguments = bundle //유저 정보를 넘겨줌

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame_seller, (fragmentClass),tag)
            .addToBackStack(tag).commit()
    }

    //MapsFragment 에서 위도 경도 정보 받음
    override fun onDataPass(latitude : Double,longitude : Double) {
        lat = latitude
        lng = longitude
        Log.d("pass", ""+latitude + longitude)
    }

    //뒤로가기버튼을 누를때 콜백
    override fun onBackPressed() {
        super.onBackPressed()
        updateBottomMenu()
    }

    //태그를 통해 현재 프래그먼트를 찾아서, 메뉴활성화
    private fun updateBottomMenu() {
        val tag1: Fragment? = supportFragmentManager.findFragmentByTag("map")
        val tag2: Fragment? = supportFragmentManager.findFragmentByTag("search")
        val tag3: Fragment? = supportFragmentManager.findFragmentByTag("manage")
        val tag4: Fragment? = supportFragmentManager.findFragmentByTag("setting")

        if (tag1 != null && tag1.isVisible) {
            bottomNavigation.menu.findItem(R.id.menu_map).isChecked = true
        }
        if (tag2 != null && tag2.isVisible) {
            bottomNavigation.menu.findItem(R.id.menu_search).isChecked = true
        }
        if (tag3 != null && tag3.isVisible) {
            bottomNavigation.menu.findItem(R.id.menu_manage).isChecked = true
        }
        if (tag4 != null && tag4.isVisible) {
            bottomNavigation.menu.findItem(R.id.menu_setting).isChecked = true
        }
    }


}