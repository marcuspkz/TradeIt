package com.example.tradeit.controller.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.databinding.ItemProductBinding
import com.example.tradeit.model.Product
import com.squareup.picasso.Picasso

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemProductBinding.bind(view)

    fun bind(product: Product) {
        binding.tvTitle.text = product.getTitle()
        binding.tvDescription.text = product.getDescription()
        binding.tvSeller.text = product.getSeller()
        binding.tvPrice.text = product.getPrice().toString() + "€"
        Picasso.get().load(product.getImage()).into(binding.ivImage)
    }
}