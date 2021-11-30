package com.jongsip.streetstall.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jongsip.streetstall.R

class StallInfoActivity: AppCompatActivity(){

    lateinit var auth: FirebaseAuth
    lateinit var uid: String
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stall_info)

        var uid =  intent.getStringExtra("stallUid")
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        val docRef = uid?.let { firestore.collection("stall").document(it) }
        if (docRef != null) {
            docRef.get().addOnSuccessListener {
                var editStallName = findViewById<TextView>(R.id.edit_stall_name)
                var editStallIntro = findViewById<TextView>(R.id.edit_stall_intro)
                editStallName.text =  it.data?.get("name").toString()
                editStallIntro.text =  it.data?.get("brief").toString()

            }
        }
    }
}