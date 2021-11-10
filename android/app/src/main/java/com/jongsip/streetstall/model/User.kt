package com.jongsip.streetstall.model

data class User(
  val userType: Int = 1, //1: customer, 2: seller
  var uid: String,
  var stall: Stall? = null
)
