package com.jongsip.streetstall.adapter

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jongsip.streetstall.fragment.MapsFragment
import com.jongsip.streetstall.model.SearchFood
import com.jongsip.streetstall.fragment.SearchFragment

import com.jongsip.streetstall.R
import com.jongsip.streetstall.activity.NavigationActivityInterface


class SearchListAdapter(
    val context: Activity?,
    val activity: NavigationActivityInterface,
    private val data: ArrayList<SearchFood>
) : BaseAdapter() {
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
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
        val view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_search_food, parent, false)
        val item = data[position]

        if (item.food.imgRef != null && context != null) {
            storageRef.child("${item.uid}/" + item.food.imgRef).downloadUrl.addOnCompleteListener {
                //Glide 라이브러리를 이용하여 이미지뷰에 uri 를 띄움
                Glide.with(context).load(it.result)
                    .into(view.findViewById(R.id.img_search_food))
            }
        }

        view.findViewById<TextView>(R.id.text_search_stall_name).text =
            item.stallName.toString()
        view.findViewById<TextView>(R.id.text_search_food_name).text =
            item.food.name.toString()
        view.findViewById<TextView>(R.id.text_search_food_info).text =
            item.food.extraInfo
        view.findViewById<TextView>(R.id.text_search_food_price).text =
            item.food.price.toString()

        val searchedFood: RelativeLayout =
            view.findViewById(com.jongsip.streetstall.R.id.searched_food)
        searchedFood.setOnClickListener {
            Log.d("가게이름 : ", item.uid)

            //uid 를 이용하여 가게 위도 경도 찾아서 MapsFragment 로 보내기
            val docRef = firestore.collection("working").document(item.uid)
            val bundle = Bundle()
            var lat: Double
            var lng: Double
            docRef.get().addOnSuccessListener {
                val lat = it.data?.get("latitude").toString().toDouble()
                val lng = it.data?.get("longitude").toString().toDouble()
                bundle.putDouble("latitude", lat)
                bundle.putDouble("longitude", lng)
            }
            MapsFragment().arguments = bundle //유저 정보를 넘겨줌

            activity.replaceFragment(MapsFragment(), "map")

        }
        return view
    }
}