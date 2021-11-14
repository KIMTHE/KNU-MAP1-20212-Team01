package com.jongsip.streetstall.model

data class Food(
    var name: String,
    var imgUrl: String? = null,
    var price: Int,
    var extraInfo: String? = null
) {
}