package com.example.tradeit.controller.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.databinding.ItemProductBinding
import com.example.tradeit.model.Product
import com.squareup.picasso.Picasso

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemProductBinding.bind(view)

    fun bind(product: Product) {
        binding.tvTitle.text = product.title
        binding.tvDescription.text = product.description
        binding.tvSeller.text = product.seller
        binding.tvPrice.text = product.price.toString() + "€"
        binding.ivImage.scaleType = ImageView.ScaleType.CENTER_CROP
        Picasso.get().load(product.image).into(binding.ivImage)
    }
}