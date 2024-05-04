package com.example.tradeit.controller.main.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tradeit.R
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ActivityProductDetailBinding
import com.example.tradeit.databinding.ActivityUserDetailBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking

class UserDetailActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseDatabase
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()
        val displayName = binding.displayName
        var sellerId: String = intent.getStringExtra("sellerId").toString()
        FirebaseFunctions.getUserById(sellerId, firebase) { user ->
            if (user != null) {
                displayName.text = user.displayName
            } else {
                // Manejar el caso donde no se pudo obtener el usuario
            }
        }
    }
}