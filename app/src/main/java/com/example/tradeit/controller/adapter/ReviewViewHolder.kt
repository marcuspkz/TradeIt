package com.example.tradeit.controller.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ItemProductBinding
import com.example.tradeit.databinding.ItemReviewBinding
import com.example.tradeit.model.Product
import com.example.tradeit.model.Review
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemReviewBinding.bind(view)
    private lateinit var firebase: FirebaseDatabase

    fun bind(review: Review) {
        firebase = FirebaseDatabase.getInstance()
        FirebaseFunctions.getUserById(review.publisherId) { user ->
            if (user != null) {
                binding.userTitle.text = user.displayName
                //imagen de perfil
                FirebaseFunctions.getUserProfilePicture(user.userId) { profilePictureUrl ->
                    profilePictureUrl?.let {
                        Picasso.get().load(it).into(binding.ivImage)
                    }
                }
            } else {
                // Manejar el caso donde no se pudo obtener el usuario
            }
        }
        binding.review.text = review.comment
        binding.postingDate.text = review.postingDate
        binding.rating.text = "Valoración: ${review.rating}"
    }
}