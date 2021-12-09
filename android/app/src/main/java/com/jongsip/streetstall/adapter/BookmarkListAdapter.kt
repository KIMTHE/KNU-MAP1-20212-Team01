package com.jongsip.streetstall.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jongsip.streetstall.R
import com.jongsip.streetstall.activity.StallInfoActivity

class BookmarkListAdapter(
    val context: Context,
    private val data: ArrayList<String>,
    private val uid: String
) : BaseAdapter() {
    var auth: FirebaseAuth = Firebase.auth
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_bookmark_stall, parent, false)
        val item = data[position]

        fireStore.collection("stall").document(item).get().addOnSuccessListener {

            view.findViewById<TextView>(R.id.text_bookmark_stall_name).text = it.data?.get("name") as String
            view.findViewById<TextView>(R.id.text_bookmark_stall_brief).text = it.data?.get("brief") as String

            view.findViewById<RelativeLayout>(R.id.layout_bookmark_stall).setOnClickListener {
                val intent = Intent(context, StallInfoActivity::class.java)
                intent.putExtra("stallUid", item)
                intent.putExtra("uid",uid)
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }

        }

        return view
    }

}