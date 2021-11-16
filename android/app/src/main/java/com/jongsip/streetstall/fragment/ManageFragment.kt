package com.jongsip.streetstall.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jongsip.streetstall.R
import com.jongsip.streetstall.activity.AddFoodActivity
import com.jongsip.streetstall.adapter.MenuListAdapter
import com.jongsip.streetstall.model.Food
import com.jongsip.streetstall.model.Stall
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageFragment : Fragment() {
    lateinit var editStallName: EditText
    lateinit var editStallIntro: EditText
    lateinit var listMenu: ListView
    lateinit var btnAddMenu: Button
    private lateinit var btnManageComplete: Button

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    lateinit var uid: String
    var foodMenu: ArrayList<Food>? = null

    companion object {
        const val ADD_REQUEST_CODE = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        uid = arguments?.getString("uid")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_manage, container, false)

        editStallName = rootView.findViewById(R.id.edit_stall_name)
        editStallIntro = rootView.findViewById(R.id.edit_stall_intro)
        listMenu = rootView.findViewById(R.id.list_menu)
        btnAddMenu = rootView.findViewById(R.id.btn_add_menu)
        btnManageComplete = rootView.findViewById(R.id.btn_manage_complete)

        val docRef = firestore.collection("stall").document(uid)
        docRef.get().addOnSuccessListener {
            editStallName.setText(it.data!!["name"].toString())
            editStallIntro.setText(it.data!!["brief"].toString())

            if (it.data!!["foodMenu"] != null)
            {
                foodMenu = convertToFood(it.data!!["foodMenu"] as ArrayList<HashMap<String, *>>)
                listMenu.adapter = MenuListAdapter(this, foodMenu!!, uid)
            }

        }

        btnAddMenu.setOnClickListener {
            startActivityForResult(Intent(activity, AddFoodActivity::class.java), ADD_REQUEST_CODE)
        }

        btnManageComplete.setOnClickListener {
            firestore.collection("stall").document(uid).set(
                Stall(
                    editStallName.text.toString(),
                    editStallIntro.text.toString(),
                    foodMenu
                )
            )
        }

        return rootView
    }

    //돌아와서 음식 메뉴 추가한 데이터 받아서 추가
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (foodMenu == null) foodMenu = ArrayList()

            val foodName = data!!.getStringExtra("name")!!
            val foodImgUri = Uri.parse(data.getStringExtra("imgUrl"))

            foodMenu!!.add(
                Food(
                    foodName,
                    uploadImage(foodImgUri, foodName),
                    data.getIntExtra("price", 0),
                    data.getStringExtra("extraInfo")
                )
            )

            listMenu.adapter = MenuListAdapter(this, foodMenu!!, uid)
        }
    }

    //Firebase Storage 에 이미지를 업로드 하는 함수.
    private fun uploadImage(uri: Uri, foodName: String): String? {
        //파일 이름 생성
        val fileName = "${foodName}_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.jpg"

        //파일 업로드, 다운로드, 삭제, 메타데이터 가져오기 또는 업데이트를 하기 위해 참조를 생성.
        //기본 참조 위치/uid/${fileName}
        val imagesRef = storageRef.child("${uid}/").child(fileName)

        //이미지 파일 업로드
        imagesRef.putFile(uri)

        return fileName
    }

    private fun convertToFood(dataFood: ArrayList<HashMap<String, *>>): ArrayList<Food> {
        val returnMenu = ArrayList<Food>()

        for (item in dataFood) {
            val tmpLong = item["price"] as Long

            returnMenu.add(
                Food(
                    item["name"] as String,
                    item["imgRef"] as String?,
                    tmpLong.toInt(),
                    item["extraInfo"] as String?
                )
            )
        }


        return returnMenu

    }


}