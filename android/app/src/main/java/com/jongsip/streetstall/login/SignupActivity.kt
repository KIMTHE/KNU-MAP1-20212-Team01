package com.jongsip.streetstall.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jongsip.streetstall.R
import com.jongsip.streetstall.model.User


class SignupActivity : AppCompatActivity() {
    lateinit var signupOkButton: Button
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var editUserType: EditText

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_signup)

        signupOkButton = findViewById(R.id.signup_okButton)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        editUserType = findViewById(R.id.edit_usertype)

        // 계정 생성 버튼
        signupOkButton.setOnClickListener {
            createAccount(editUserType.text.toString().toInt(),editEmail.text.toString(), editPassword.text.toString())
        }
    }

    // 계정 생성
    private fun createAccount(userType: Int, email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val uid = task.result.user?.uid
                        val userModel = User(userType, uid!!, null)

                        // database 에 저장
                        firestore.collection("user").document(uid).set(userModel)

                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}