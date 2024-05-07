package com.example.tradeit.controller.main.detail

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.example.tradeit.R
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityProductDetailBinding
import com.example.tradeit.model.Review
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
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
        val sellerName = binding.userName
        val sellerImage = binding.sellerImage
        val deleteButton = binding.deleteButton
        val chatButton = binding.chatButton
        val reviewButton = binding.reviewButton

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

        fun showReviewDialog() {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_review)

            val publishButton: Button = dialog.findViewById(R.id.publishButton)
            val backButton: Button = dialog.findViewById(R.id.backButton)
            val rating: Spinner = dialog.findViewById(R.id.ratingSpinner)
            val review: EditText = dialog.findViewById(R.id.review)

            backButton.setOnClickListener {
                dialog.dismiss()
            }

            publishButton.setOnClickListener {
                val review = Review("", rating.selectedItem.toString().toInt(), review.text.toString(), GlobalFunctions.getCurrentDate(), FirebaseFunctions.getDisplayName(true).toString(), sellerId.toString(), productId, null)
                FirebaseFunctions.addReview(review, "IuUh3LZgSpTAqFBivJ5uiDdrQEO2", firebase)
            }

            dialog.show()
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

        chatButton.setOnClickListener {

        }

        reviewButton.setOnClickListener {
            showReviewDialog()
        }
    }
}