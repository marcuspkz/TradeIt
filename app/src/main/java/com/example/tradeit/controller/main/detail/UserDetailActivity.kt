package com.example.tradeit.controller.main.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.R
import com.example.tradeit.controller.adapter.ProductAdapter
import com.example.tradeit.controller.adapter.ReviewAdapter
import com.example.tradeit.controller.main.publish.NewProductActivity
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ActivityProductDetailBinding
import com.example.tradeit.databinding.ActivityUserDetailBinding
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking

class UserDetailActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseDatabase
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()

        reviewAdapter = ReviewAdapter()
        binding.rvReviews.setHasFixedSize(true)
        binding.rvReviews.layoutManager = LinearLayoutManager(this)
        binding.rvReviews.adapter = reviewAdapter

        val displayName = binding.displayName
        var sellerId: String = intent.getStringExtra("sellerId").toString()
        FirebaseFunctions.getUserById(sellerId, firebase) { user ->
            if (user != null) {
                displayName.text = user.displayName
            } else {
                // Manejar el caso donde no se pudo obtener el usuario
            }
        }

        FirebaseFunctions.getAllReviewsForUser(sellerId, firebase, reviewAdapter)
        //meter addbutton para insertar reseña
        /*binding.addButton.setOnClickListener {

        }*/
    }
}