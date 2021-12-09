package com.jongsip.streetstall.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jongsip.streetstall.R
import com.jongsip.streetstall.adapter.MenuListAdapter
import com.jongsip.streetstall.model.BookMark
import com.jongsip.streetstall.model.Food
import com.jongsip.streetstall.util.FirebaseUtil
import org.w3c.dom.Text
import java.util.HashMap

class StallInfoActivity: AppCompatActivity(){

    lateinit var textStallName: TextView
    lateinit var textStallIntro: TextView
    lateinit var listMenu: ListView
    lateinit var listReview: ListView
    lateinit var btnAddReview: Button
    lateinit var imgBookmark: ImageView

    private lateinit var auth: FirebaseAuth
    lateinit var uid: String
    lateinit var stallUid: String
    lateinit var firestore: FirebaseFirestore

    lateinit var adapter: MenuListAdapter
    var foodMenu: ArrayList<Food> = ArrayList<Food>()
    var statusBookmark = false
    var bookmarkArray: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stall_info)

        uid = intent.getStringExtra("uid").toString()
        stallUid = intent.getStringExtra("stallUid").toString()
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        textStallName = findViewById(R.id.text_stall_name)
        textStallIntro = findViewById(R.id.text_stall_intro)
        listMenu = findViewById(R.id.list_menu)
        listReview = findViewById(R.id.list_review)
        btnAddReview = findViewById(R.id.btn_add_review)
        imgBookmark = findViewById(R.id.img_bookmark)

        //해당 카드뷰의 노점 정보를 받아서 view 에 세팅
        val docRef = stallUid.let { firestore.collection("stall").document(it) }
        docRef.get().addOnSuccessListener {
            textStallName.text =  it.data!!["name"].toString()
            textStallIntro.text =  it.data!!["brief"].toString()

            if (it.data!!["foodMenu"] != null ) {
                foodMenu =
                    FirebaseUtil.convertToFood(it.data!!["foodMenu"] as ArrayList<HashMap<String, *>>)
                adapter = MenuListAdapter(applicationContext, foodMenu, stallUid)
                listMenu.adapter = adapter
            }
        }

        //고객일 때, 북마크버튼 활성화
        firestore.collection("user").document(uid).get().addOnSuccessListener { userDoc->
            if(userDoc.data!!["userType"] == 1.toLong()){
                imgBookmark.visibility = View.VISIBLE

                firestore.collection("bookmark").document(uid).get().addOnSuccessListener { bmDoc->
                    bookmarkArray = bmDoc.data!!["uidArray"] as ArrayList<String>?

                    if(bookmarkArray != null && bookmarkArray!!.contains(stallUid)){
                        statusBookmark = true
                        imgBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
                    }

                    else if(bookmarkArray == null) bookmarkArray = ArrayList()
                }
            }
        }

        imgBookmark.setOnClickListener {
            if(!statusBookmark){
                statusBookmark = true
                imgBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)

                bookmarkArray!!.add(stallUid)
            }

            else{
                statusBookmark = false
                imgBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)

                bookmarkArray!!.remove(stallUid)
            }

            firestore.collection("bookmark").document(uid).set(BookMark(bookmarkArray!!))
        }

    }

}