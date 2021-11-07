package com.jongsip.streetstall.model

data class Stall(
    var name: String,
    var brief: String,
    var foodMenu: ArrayList<Food>
) {
}