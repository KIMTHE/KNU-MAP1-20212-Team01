package com.jongsip.streetstall.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jongsip.streetstall.R
import com.jongsip.streetstall.activity.CustomerMainActivity
import com.jongsip.streetstall.activity.SellerMainActivity

class LoginActivity : AppCompatActivity() {
    lateinit var btnLogin: Button
    lateinit var btnSignup: Button
    lateinit var editLoginID: EditText
    lateinit var editLoginPW: EditText

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btn_login)
        btnSignup = findViewById(R.id.btn_signup)
        editLoginID = findViewById(R.id.edit_login_id)
        editLoginPW = findViewById(R.id.edit_login_pw)

        // 회원가입 창으로
        btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // 로그인 버튼
        btnLogin.setOnClickListener {
            signIn(editLoginID.text.toString(), editLoginPW.text.toString())
        }

    }

//    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
//    public override fun onStart() {
//        super.onStart()
//        moveMainPage(auth.currentUser)
//    }

    // 로그인
    private fun signIn(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        toastShow("로그인에 성공 하였습니다.")
                        moveMainPage(auth.currentUser)
                    } else {
                        toastShow("로그인에 실패 하였습니다.")
                    }
                }
        }
    }


    // 유저정보를 받아서, 판매자인지 고객인지 판단 후 해당되는 메인 액티비티 호출
    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            val uid = user.uid
            val docRef = firestore.collection("user").document(uid)

            docRef.get().addOnSuccessListener {
                if (it != null) {
                    val intentMain = if (it.data!!["userType"] == 1.toLong())
                        Intent(this, CustomerMainActivity::class.java)
                    else
                        Intent(this, SellerMainActivity::class.java)

                    intentMain.putExtra("uid",uid)
                    startActivity(intentMain)
                    finish()
                } else {
                    toastShow("해당 유저정보가 없습니다.")
                }
            }
        }
    }

    //토스트 메시지
    private fun toastShow(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}