package com.jongsip.streetstall.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jongsip.streetstall.R
import com.jongsip.streetstall.model.SearchFood

class SearchListAdapter(val context: Context, private val data: ArrayList<SearchFood>): BaseAdapter() {
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
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_search_food,parent,false)
        val item = data[position]

        if(item.food.imgRef != null){
            storageRef.child("${item.uid}/"+item.food.imgRef).downloadUrl.addOnCompleteListener {
                //Glide 라이브러리를 이용하여 이미지뷰에 uri 를 띄움
                Glide.with(context).load(it.result).into(view.findViewById(R.id.img_search_food))
            }
        }

        view.findViewById<TextView>(R.id.text_search_stall_name).text =
            String.format(context.getString(R.string.item_stall_name_text),item.stallName )
        view.findViewById<TextView>(R.id.text_search_food_name).text =
            String.format(context.getString(R.string.item_food_name_text), item.food.name)
        view.findViewById<TextView>(R.id.text_search_food_info).text = item.food.extraInfo
        view.findViewById<TextView>(R.id.text_search_food_price).text =
            String.format(context.getString(R.string.item_food_price_text),item.food.price)

        return view
    }


}