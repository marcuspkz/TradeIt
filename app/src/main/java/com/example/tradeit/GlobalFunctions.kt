package com.example.tradeit

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object GlobalFunctions {
    //debug
    fun getDisplayName(): String? {
        val user = Firebase.auth.currentUser
        var name: String?
        name = ""
        user?.let {
            for (profile in it.providerData) {
                name = profile.displayName
            }
        }

        return name
    }

    fun getEmail(): String? {
        val user = Firebase.auth.currentUser
        var name: String?
        name = ""
        user?.let {
            for (profile in it.providerData) {
                name = profile.email
            }
        }

        return name
    }
}