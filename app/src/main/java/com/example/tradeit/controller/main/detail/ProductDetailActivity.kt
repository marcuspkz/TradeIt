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
import com.example.tradeit.controller.main.chat.ChatActivity
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
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val sellerName = binding.userName
        val sellerImage = binding.sellerImage
        val deleteButton = binding.deleteButton
        val chatButton = binding.chatButton
        val reviewButton = binding.reviewButton

        deleteButton.visibility = GONE
        reviewButton.visibility = GONE
        chatButton.visibility = GONE
        var sellerId: String? = ""

        var productId: String = intent.getStringExtra("productId").toString()
        if (!productId.isNullOrEmpty()) {
            FirebaseFunctions.getProduct(productId) { product ->
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
                        } else {
                            reviewButton.visibility = VISIBLE
                            chatButton.visibility = VISIBLE
                        }
                    }
                    sellerId?.let {
                        FirebaseFunctions.getUserProfilePicture(it) { profilePictureUrl ->
                            profilePictureUrl?.let {
                                Picasso.get().load(it).into(binding.sellerImage)
                            }
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
                val review = Review(
                    "",
                    rating.selectedItem.toString().toInt(),
                    review.text.toString(),
                    GlobalFunctions.getCurrentDate(),
                    FirebaseFunctions.getDisplayName(true).toString(),
                    sellerId.toString(),
                    productId,
                    null
                )
                FirebaseFunctions.addReview(review, sellerId.toString(), firebase)
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
                    FirebaseFunctions.deleteProductById(productId, this)
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        chatButton.setOnClickListener {
            firebaseAuth.currentUser?.let { it1 ->
                FirebaseFunctions.chatExists(
                    it1.uid,
                    sellerId.toString(),
                    productId
                ) { chatExists ->
                    if (!chatExists) {
                        //crear nuevo chat
                        firebaseAuth.currentUser?.let { currentUser ->
                            FirebaseFunctions.createChat(
                                currentUser.uid,
                                sellerId.toString(),
                                productId
                            ) { chatId ->
                                if (chatId != null) {
                                    //chat creado
                                    val intent = Intent(this, ChatActivity::class.java)
                                    intent.putExtra("chatId", chatId)
                                    intent.putExtra("toUser", sellerId)
                                    intent.putExtra("fromUser", currentUser.uid)
                                    intent.putExtra("relatedProduct", productId)
                                    startActivity(intent)
                                } else {
                                    // Ocurrió un error al crear el chat
                                }
                            }
                        }
                    } else {
                        //TODO: gestionar recuperación de mensajes, se hace en la otra activity
                        //el chat ya existe
                        GlobalFunctions.showInfoDialog(this, "Error", "El chat ya existe.")
                    }
                }
            }

            reviewButton.setOnClickListener {
                showReviewDialog()
            }
        }
    }
}