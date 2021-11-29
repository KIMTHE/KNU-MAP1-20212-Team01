package com.jongsip.streetstall.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jongsip.streetstall.R
import com.jongsip.streetstall.fragment.*
import com.jongsip.streetstall.util.PermissionUtil
import kotlin.system.exitProcess

class CustomerMainActivity : AppCompatActivity(), MapsFragment.OnDataPassListener, NavigationActivityInterface {

    private lateinit var bottomNavigation: BottomNavigationView
    lateinit var uid: String

    //최근에 뒤로가기 버튼을 누른 시각
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_customer)

        uid = intent.getStringExtra("uid")!!
        bottomNavigation = findViewById(R.id.bottom_navi_customer)

        replaceFragment(MapsFragment(),"map",0.0,0.0)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_map -> replaceFragment(MapsFragment(),"map",0.0,0.0)
                R.id.menu_search -> replaceFragment(SearchFragment(),"search",0.0,0.0)
                R.id.menu_manage -> replaceFragment(BookmarkFragment(),"bookmark",0.0,0.0)
                else -> replaceFragment(SettingFragment(),"setting",0.0,0.0)
            }
            true
        }

        PermissionUtil.requestLocationPermission(this)//위치 권한 요청
    }

    override fun replaceFragment(fragmentClass: Fragment, tag: String, lat: Double , lng:Double) {
        val bundle = Bundle()
        bundle.putString("uid", uid)
        bundle.putDouble("latitude", lat)
        bundle.putDouble("longitude", lng)
        fragmentClass.arguments = bundle //유저 정보를 넘겨줌

        supportFragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame_customer, (fragmentClass),tag)
            .addToBackStack(tag).commit()
    }

    //MapsFragment에서 위도 경도 정보 받음
    override fun onDataPass(latitude : Double,longitude : Double) {
       // lat = latitude
       // lng = longitude
       // Log.d("pass", ""+latitude + longitude)
    }

    //뒤로가기버튼을 누를때 콜백
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            val tempTime = System.currentTimeMillis()
            val intervalTime = tempTime - backPressedTime

            //2초이내 한번 더 뒤로가기 눌렀을 때, 종료
            if (!(0 > intervalTime || 2000 < intervalTime)) {
                finishAffinity()
                System.runFinalization()
                exitProcess(0)
            } else {
                backPressedTime = tempTime
                Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return
            }
        }
        super.onBackPressed()
        updateBottomMenu()
    }

    //태그를 통해 현재 프래그먼트를 찾아서, 메뉴활성화
    override fun updateBottomMenu() {
        val tag1: Fragment? = supportFragmentManager.findFragmentByTag("map")
        val tag2: Fragment? = supportFragmentManager.findFragmentByTag("search")
        val tag3: Fragment? = supportFragmentManager.findFragmentByTag("bookmark")
        val tag4: Fragment? = supportFragmentManager.findFragmentByTag("setting")

        if (tag1 != null && tag1.isVisible) {
            bottomNavigation.menu.findItem(R.id.menu_map).isChecked = true
        }
        if (tag2 != null && tag2.isVisible) {
            bottomNavigation.menu.findItem(R.id.menu_search).isChecked = true
        }
        if (tag3 != null && tag3.isVisible) {
            bottomNavigation.menu.findItem(R.id.menu_bookmark).isChecked = true
        }
        if (tag4 != null && tag4.isVisible) {
            bottomNavigation.menu.findItem(R.id.menu_setting).isChecked = true
        }
    }

}