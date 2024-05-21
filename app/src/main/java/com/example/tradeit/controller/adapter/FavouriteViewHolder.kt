package com.example.tradeit.controller.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ItemFavouriteBinding
import com.example.tradeit.databinding.ItemProductBinding
import com.example.tradeit.databinding.ItemReviewBinding
import com.example.tradeit.model.Favourite
import com.example.tradeit.model.Product
import com.example.tradeit.model.Review
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemFavouriteBinding.bind(view)
    private lateinit var firebase: FirebaseDatabase

    fun bind(favourite: Favourite) {
        firebase = FirebaseDatabase.getInstance()
        if (favourite.isProduct) {
            favourite.itemId?.let {
                FirebaseFunctions.getProductById(it) { product ->
                    if (product != null) {
                        binding.favTitle.text = product.title
                        binding.favDescription.text = product.description
                        binding.favSeller.text = product.seller
                        binding.favPrice.text = "${product.price}"
                        Picasso.get().load(product.image).into(binding.ivImage)
                        binding.cardView.setCardBackgroundColor(Color.parseColor("#D1FFDE"))
                    } else {
                        //no se obtuvo producto
                    }
                }
            }
        } else {
            favourite.itemId?.let {
                FirebaseFunctions.getServiceById(it) { service ->
                    if (service != null) {
                        binding.favTitle.text = service.title
                        binding.favDescription.text = service.description
                        binding.favSeller.text = service.contact
                        binding.favPrice.text = "${service.price}"
                        Picasso.get().load(service.image).into(binding.ivImage)
                        binding.cardView.setCardBackgroundColor(Color.parseColor("#CCEEFF"))
                    } else {
                        //no se obtuvo servicio
                    }
                }
            }
        }

        //TODO: obtención de servicios
    }
}