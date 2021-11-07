package com.jongsip.streetstall.model

data class User(
  val userType: String, //1: buyer, 2: seller
  var uid: String,
  var stall: Stall? = null
)
