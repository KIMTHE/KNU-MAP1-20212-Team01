package com.jongsip.streetstall.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jongsip.streetstall.R
import com.jongsip.streetstall.activity.AddFoodActivity
import com.jongsip.streetstall.adapter.MenuListAdapter
import com.jongsip.streetstall.model.Food
import com.jongsip.streetstall.model.Stall

class ManageFragment : Fragment() {
    lateinit var editStallName: EditText
    lateinit var editStallIntro: EditText
    lateinit var listMenu: ListView
    lateinit var btnAddMenu: Button
    private lateinit var btnManageComplete: Button

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var uid: String
    var foodMenu: ArrayList<Food>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        uid = arguments?.getString("uid")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_manage, container, false)

        editStallName = rootView.findViewById(R.id.edit_stall_name)
        editStallIntro = rootView.findViewById(R.id.edit_stall_intro)
        listMenu = rootView.findViewById(R.id.list_menu)
        btnAddMenu = rootView.findViewById(R.id.btn_add_menu)
        btnManageComplete = rootView.findViewById(R.id.btn_manage_complete)

        val docRef = firestore.collection("stall").document(uid)
        docRef.get().addOnSuccessListener {
            editStallName.text = it.data!!["name"] as Editable
            editStallIntro.text = it.data!!["brief"] as Editable
            foodMenu = it.data!!["foodMenu"] as ArrayList<Food>?

            if(foodMenu != null)
                listMenu.adapter = MenuListAdapter(foodMenu!!)
        }

        btnAddMenu.setOnClickListener {
            startActivity(Intent(activity,AddFoodActivity::class.java))
        }

        btnManageComplete.setOnClickListener {
            firestore.collection("stall").document(uid).set(Stall(
                editStallName.text.toString(),
                editStallIntro.text.toString(),
                foodMenu
            ))
        }



        return rootView
    }

    //돌아와서 음식 메뉴 추가한 데이터 받아서 추가
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(foodMenu == null) foodMenu = ArrayList()

            foodMenu!!.add(Food(
                data!!.getStringExtra("name")!!,
                data.getStringExtra("imgUrl"),
                data.getIntExtra("price",0),
                data.getStringExtra("extraInfo")
            ))
        }
    }

}