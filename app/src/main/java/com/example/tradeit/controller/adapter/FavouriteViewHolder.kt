package com.example.tradeit.controller.adapter

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
        favourite.itemId?.let {
            FirebaseFunctions.getProductById(it) { product ->
                if (product != null) {
                    binding.favTitle.text = product.title
                    binding.favDescription.text = product.description
                    binding.favSeller.text = product.seller
                    binding.favPrice.text = "${product.price}€"
                    Picasso.get().load(product.image).into(binding.ivImage)
                } else {
                    // Manejar el caso donde no se pudo obtener el producto
                }
            }
        }
        //TODO: obtención de servicios
    }
}