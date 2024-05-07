package com.example.tradeit.controller.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.databinding.ItemProductBinding
import com.example.tradeit.databinding.ItemReviewBinding
import com.example.tradeit.model.Product
import com.example.tradeit.model.Review
import com.squareup.picasso.Picasso

class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemReviewBinding.bind(view)

    fun bind(review: Review) {
        binding.userTitle.text = review.profileId
        binding.review.text = review.comment
        binding.postingDate.text = review.postingDate
        binding.rating.text = "Valoración: ${review.postingDate}"
        //Picasso.get().load(product.getImage()).into(binding.ivImage)
    }
}