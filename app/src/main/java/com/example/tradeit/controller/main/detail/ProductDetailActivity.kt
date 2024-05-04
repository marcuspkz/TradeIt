package com.example.tradeit.controller.main.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.tradeit.R
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ActivityProductDetailBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()
        val sellerName = binding.userName
        val sellerImage = binding.sellerImage
        val deleteButton = binding.deleteButton
        deleteButton.visibility = GONE
        var sellerId: String? = null

        var productId: String = intent.getStringExtra("productId").toString()
        if (!productId.isNullOrEmpty()) {
            FirebaseFunctions.getProduct(productId, firebase) { product ->
                if (product != null) {
                    binding.title.text = product.getTitle()
                    binding.image.adjustViewBounds = true
                    binding.image.scaleType = ImageView.ScaleType.FIT_XY
                    Picasso.get().load(product.getImage()).into(binding.image)
                    binding.price.text = "${product.getPrice()}€"
                    binding.description.text = product.getDescription()
                    binding.userName.text = product.getSeller()
                    binding.postingDate.text = "Publicado el ${product.getPostingDate()}"
                    binding.ubication.text = "Ubicación: ${product.getUbication()}"
                    binding.category.text = "Categoría: ${product.getCategory()}"
                    sellerId = product.getSellerId()
                    val actualUser = FirebaseAuth.getInstance().currentUser
                    if (actualUser != null) {
                        if (sellerId == actualUser.uid) {
                            deleteButton.visibility = VISIBLE
                        }
                    }
                }
            }
        }

        sellerName.setOnClickListener {
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("sellerId", sellerId)
            startActivity(intent)
        }

        sellerImage.setOnClickListener {
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("sellerId", sellerId)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("¿Seguro que quieres borrar el producto?")
                .setPositiveButton("Sí") { _, _ ->
                    FirebaseFunctions.deleteProductById(productId, firebase, this)
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }
}