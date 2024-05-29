package com.example.tradeit.controller.adapter

import android.content.Intent
import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.R
import com.example.tradeit.controller.main.detail.ProductDetailActivity
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.model.Favourite
import com.example.tradeit.model.Product
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProductAdapter(private var productList: List<Product> = emptyList()) : RecyclerView.Adapter<ProductViewHolder>() {
    fun updateList(list: List<Product>) {
        productList = list.sortedByDescending { it.postingDate }
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        var firebaseAuth: FirebaseAuth
        holder.bind(productList[position])
        holder.itemView.setOnLongClickListener {
            firebaseAuth = FirebaseAuth.getInstance()
            val userId = firebaseAuth.currentUser?.uid.toString()
            val fav = Favourite("", userId, productList[position].productId, true)
            val context = holder.itemView.context
            FirebaseFunctions.favouriteExists(productList[position].productId) { exists ->
                if (exists) {
                    GlobalFunctions.showInfoDialog(
                        context,
                        "Error.",
                        "El producto ${productList[position].title} ya está en favoritos."
                    )
                } else {
                    if (FirebaseFunctions.addFavourite(fav) != null) {
                        GlobalFunctions.showInfoDialog(
                            context,
                            "¡Favorito añadido!",
                            "Se ha añadido el producto ${productList[position].title} a favoritos."
                        )
                    } else {
                        GlobalFunctions.showInfoDialog(
                            context,
                            "Error.",
                            "Hubo un problema al añadir el producto a favoritos."
                        )
                    }
                }
            }
            true
        }
        holder.itemView.setOnClickListener {
            productList[position].sellerId?.let { it1 ->
                FirebaseFunctions.userExists(it1) { exists ->
                    if (exists) {
                        val intent = Intent(holder.itemView.context, ProductDetailActivity::class.java)
                        intent.putExtra("productId", productList[position].productId)
                        holder.itemView.context.startActivity(intent)
                    } else {
                        val context = holder.itemView.context
                        GlobalFunctions.showInfoDialog(context, "Error", "Este usuario no existe.")
                    }
                }
            }
        }
    }
    override fun getItemCount() = productList.size
}