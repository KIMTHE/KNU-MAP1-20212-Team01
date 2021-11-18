package com.jongsip.streetstall.model

data class SearchFood(
    val uid: String,
    val stallName: String,
    val food: Food,
    val workingPosition: WorkingPosition
) {
}