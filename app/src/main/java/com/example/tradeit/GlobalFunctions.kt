package com.example.tradeit

import com.example.tradeit.model.Product
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

object GlobalFunctions {
    private lateinit var firebase: FirebaseDatabase

    fun setFirebase() {
        firebase = FirebaseDatabase.getInstance()
    }
    fun addProduct(product: Product) {
        val databaseReference = firebase.reference
        databaseReference.child("Products").push().setValue(product)
    }

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