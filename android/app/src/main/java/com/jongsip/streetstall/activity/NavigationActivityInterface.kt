package com.jongsip.streetstall.activity

import androidx.fragment.app.Fragment

interface NavigationActivityInterface {

    fun replaceFragment(fragmentClass: Fragment, tag: String, lat:Double, lng:Double)

    fun updateBottomMenu()

}