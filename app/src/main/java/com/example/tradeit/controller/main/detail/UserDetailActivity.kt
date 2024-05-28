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
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityProductDetailBinding
import com.example.tradeit.databinding.ActivityUserDetailBinding
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.runBlocking

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reviewAdapter = ReviewAdapter(mutableListOf())
        binding.rvReviews.setHasFixedSize(true)
        binding.rvReviews.layoutManager = LinearLayoutManager(this)
        binding.rvReviews.adapter = reviewAdapter

        val displayName = binding.displayName
        var sellerId: String = intent.getStringExtra("sellerId").toString()
        FirebaseFunctions.getUserById(sellerId) { user ->
            if (user != null) {
                displayName.text = user.displayName
                Picasso.get().load(user.profilePicture).into(binding.profilePicture)
            } else {
                GlobalFunctions.showInfoDialog(this, "Error", "No se pudo obtener el usuario.")
            }
        }

        FirebaseFunctions.averageRating(sellerId) { avgRating ->
            if (avgRating != 0.0) {
                binding.rating.text = "Valoración media: ${String.format("%.1f", avgRating)} ★"
            } else {
                binding.rating.text = "Este usuario no tiene valoraciones."
            }
        }

        FirebaseFunctions.getAllReviewsForUser(sellerId, reviewAdapter)
    }
}