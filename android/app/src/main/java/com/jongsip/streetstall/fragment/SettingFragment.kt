package com.jongsip.streetstall.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseUser
import com.jongsip.streetstall.R
import com.jongsip.streetstall.login.LoginActivity

class SettingFragment : Fragment() {

    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = arguments?.getString("uid")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    companion object {

    }
}


//// 로그아웃 - 다른 activity 에서도 사용
//logoutbutton.setOnClickListener {
//    // 로그인 화면으로
//    val intent = Intent(this, LoginActivity::class.java)
//    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//    startActivity(intent)
//    auth?.signOut()
//}