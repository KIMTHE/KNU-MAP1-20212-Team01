package com.jongsip.streetstall.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import com.jongsip.streetstall.R
import com.jongsip.streetstall.model.Food

class MenuListAdapter(private val data: ArrayList<Food>): BaseAdapter() {

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

        view.findViewById<TextView>(R.id.text_menu_food_name).text = item.name
        //view.findViewById<ImageView>(R.id.img_menu_food).setImageResource(item.imgUrl!!)
        view.findViewById<TextView>(R.id.text_menu_food_info).text = item.extraInfo
        view.findViewById<TextView>(R.id.text_menu_food_price).text = item.price.toString()

        return view
    }


}