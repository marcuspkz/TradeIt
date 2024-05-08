package com.example.tradeit.model

data class User (
    val userId: String,
    val displayName: String,
    val email: String,
    val profilePicture: String,
) {
    constructor() : this("", "", "", "")
}