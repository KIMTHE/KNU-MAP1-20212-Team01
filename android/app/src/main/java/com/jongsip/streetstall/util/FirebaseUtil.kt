package com.jongsip.streetstall.util

import com.jongsip.streetstall.model.Food
import java.util.HashMap

object FirebaseUtil {

    //firestore, stall document 안에 foodMenu 필드를, Food 배열로 변환
    fun convertToFood(dataFood: ArrayList<HashMap<String, *>>): ArrayList<Food> {
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