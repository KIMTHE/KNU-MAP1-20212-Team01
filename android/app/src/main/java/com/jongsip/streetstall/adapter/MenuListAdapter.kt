package com.jongsip.streetstall.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jongsip.streetstall.R
import com.jongsip.streetstall.fragment.ManageFragment
import com.jongsip.streetstall.model.Food

class MenuListAdapter(val context: ManageFragment, private val data: ArrayList<Food>, private val uid: String): BaseAdapter() {
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
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_menu_food,parent,false)
        val item = data[position]

        if(item.imgRef != null){
            storageRef.child("${uid}/"+item.imgRef).downloadUrl.addOnCompleteListener {
                //Glide 라이브러리를 이용하여 이미지뷰에 uri 를 띄움
                Glide.with(context).load(it.result).into(view.findViewById(R.id.img_menu_food))
            }
        }

        view.findViewById<TextView>(R.id.text_menu_food_name).text =
            String.format(context.getString(R.string.item_food_name_text), item.name)
        view.findViewById<TextView>(R.id.text_menu_food_info).text = item.extraInfo
        view.findViewById<TextView>(R.id.text_menu_food_price).text =
            String.format(context.getString(R.string.item_food_price_text),item.price)

        return view
    }

}