package com.example.tradeit.controller.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.databinding.ItemProductBinding
import com.example.tradeit.databinding.ItemServiceBinding
import com.example.tradeit.model.Product
import com.example.tradeit.model.Service
import com.squareup.picasso.Picasso

class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemServiceBinding.bind(view)

    fun bind(service: Service) {
        binding.tvTitle.text = service.title
        binding.tvDescription.text = service.description
        binding.tvPrice.text = service.price
        binding.ivImage.scaleType = ImageView.ScaleType.CENTER_CROP
        Picasso.get().load(service.image).into(binding.ivImage)
    }
}