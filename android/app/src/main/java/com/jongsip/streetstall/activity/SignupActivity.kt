package com.jongsip.streetstall.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jongsip.streetstall.R
import com.jongsip.streetstall.model.BookMark
import com.jongsip.streetstall.model.Stall
import com.jongsip.streetstall.model.User


class SignupActivity : AppCompatActivity() {
    lateinit var signupOkButton: Button
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var rdGroupUserType: RadioGroup

    lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    var userType = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_signup)

        signupOkButton = findViewById(R.id.btn_signup_complete)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        rdGroupUserType = findViewById(R.id.rd_group_userType)

        rdGroupUserType.setOnCheckedChangeListener { group, checkedId ->
            userType = when(checkedId){
                R.id.rd_btn_customer -> 1
                else -> 2
            }
        }

        // 계정 생성 버튼
        signupOkButton.setOnClickListener {
            createAccount(
                userType,
                editEmail.text.toString(),
                editPassword.text.toString()
            )
        }
    }

    // 계정 생성
    private fun createAccount(userType: Int, email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val uid = task.result.user?.uid
                        val userModel = User(userType, null)

                        // database 에 저장
                        fireStore.collection("user").document(uid!!).set(userModel)

                        if (userType == 1) //고객 일 시, bookmark 생성
                            fireStore.collection("bookmark").document(uid).set(BookMark(ArrayList<String>()))
                        else if (userType == 2) //노점주 일 시, stall 생성
                            fireStore.collection("stall").document(uid).set(Stall("", ""))

                        toastShow("계정 생성 완료")
                        finish() // 가입창 종료
                    } else {
                        toastShow("계정 생성 실패")
                    }
                }
        }
    }

    //토스트 메시지
    private fun toastShow(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}