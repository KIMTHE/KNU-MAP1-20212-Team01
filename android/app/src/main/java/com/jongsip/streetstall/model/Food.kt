package com.jongsip.streetstall.model

data class Food(
    var name: String,
    var imgRef: String? = null,
    var price: Int,
    var extraInfo: String? = null
) {
}