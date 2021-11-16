package com.jongsip.streetstall.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jongsip.streetstall.R
import com.jongsip.streetstall.login.LoginActivity
import com.jongsip.streetstall.model.workingPosition

class SettingFragment : Fragment() {

    lateinit var open_close: ToggleButton
    lateinit var workingend: Button
    lateinit var auth: FirebaseAuth
    lateinit var uid: String
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = arguments?.getString("uid")!!
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_setting, container, false)
        var lat = 0.0
        var lng = 0.0

        //SellerMainAvtivity에서 위도 경도 정보 가져옴
        lat = arguments?.getDouble("latitude") ?:0.0
        lng = arguments?.getDouble("longitude")?:0.0
        Log.d("lat", ""+lat)
        Log.d("lng", ""+lng)

        open_close = rootView.findViewById(R.id.open_close)
        //앱 껐다 켜도 서버에서 데이터 받아와서 데이터있으면 영업종료버튼이 바로 보이게 함
        val docRef = firestore.collection("working").document(uid)
        docRef.get().addOnSuccessListener {
            if(it.data!!["latitude"] != null){
                if(!open_close.isChecked){
                    open_close.toggle()
                }
            }
        }
        open_close.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){//영업시작 버튼 눌렀을 때
                firestore.collection("working").document(uid).set(workingPosition(lat, lng))
            }
            else{//영업종료 버튼 눌렀을 때
                firestore.collection("working").document(uid)
                    .delete()
                    .addOnSuccessListener { Log.d("delete", "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w("delete", "Error deleting document", e) }        }
        }
        return rootView
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