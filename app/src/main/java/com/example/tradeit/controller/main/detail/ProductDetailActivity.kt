package com.example.tradeit.controller.main.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tradeit.R
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ActivityProductDetailBinding
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseDatabase
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()

        var productId: String = intent.getStringExtra("productId").toString()
        if (!productId.isNullOrEmpty()) {
            FirebaseFunctions.getProduct(productId, firebase) { product ->
                if (product != null) {
                    binding.title.text = product.getTitle()
                    Picasso.get().load(product.getImage()).into(binding.image)
                    binding.price.text = "${product.getPrice()}€"
                    binding.description.text = product.getDescription()
                    binding.userName.text = product.getSeller()
                    binding.postingDate.text = "Publicado el ${product.getPostingDate()}"
                    binding.ubication.text = "Ubicación: ${product.getUbication()}"
                    binding.category.text = "Categoría: ${product.getCategory()}"
                }
            }
        }

        /*
        if (product != null) {
            binding.title.text = product.getTitle()
            Picasso.get().load(product.getImage()).into(binding.image)
            binding.price.text = product.getPrice().toString() + "€"
            binding.description.text = product.getDescription()
            //imagen de perfil del usuario
            binding.userName.text = product.getSeller()
            binding.postingDate.text = "Publicado el " + product.getPostingDate()
            binding.ubication.text = "Ubicación: " + product.getUbication()
            binding.ubication.text = "Categoría: " + product.getCategory()
        }*/
    }
}