package com.example.tradeit.controller.main.detail

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tradeit.R
import com.example.tradeit.controller.main.chat.ChatActivity
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityServiceDetailBinding
import com.example.tradeit.model.Favourite
import com.example.tradeit.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ServiceDetailActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityServiceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        initUI()
    }

    private fun initUI() {
        val deleteButton = binding.deleteButton
        val chatButton = binding.chatButton
        val reviewButton = binding.reviewButton

        deleteButton.visibility = View.GONE
        reviewButton.visibility = View.GONE
        chatButton.visibility = View.GONE
        var contactId: String? = ""

        var serviceId: String = intent.getStringExtra("serviceId").toString()
        if (!serviceId.isNullOrEmpty()) {
            FirebaseFunctions.getService(serviceId) { service ->
                if (service != null) {
                    binding.title.text = service.title
                    binding.image.adjustViewBounds = true
                    binding.image.scaleType = ImageView.ScaleType.FIT_XY
                    binding.price.text = "${service.price}"
                    binding.description.text = service.description
                    Picasso.get().load(service.image).into(binding.image)
                    binding.postingDate.text = "Publicado el ${GlobalFunctions.formatDateForPosting(service.postingDate)}"
                    binding.ubication.text = "Ubicación: ${service.location}"
                    binding.category.text = "Categoría: ${service.category}"
                    binding.duration.text = "Duración del servicio: ${service.duration} horas"
                    binding.requirementsText.text = "Requisitos adicionales: ${service.requirements}"
                    contactId = service.contactId

                    FirebaseFunctions.getUserById(contactId!!) { user ->
                        if (user != null) {
                            binding.userName.text = user.displayName
                            Picasso.get().load(user.profilePicture).into(binding.sellerImage)
                        }
                    }

                    val actualUser = FirebaseAuth.getInstance().currentUser
                    if (actualUser != null) {
                        if (contactId == actualUser.uid) {
                            deleteButton.visibility = View.VISIBLE
                        } else {
                            reviewButton.visibility = View.VISIBLE
                            chatButton.visibility = View.VISIBLE
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
                    contactId.toString(),
                    serviceId,
                    null
                )
                FirebaseFunctions.addReview(review, contactId.toString()) { success ->
                    if (success) {
                        Toast.makeText(this, "Reseña publicada correctamente.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Ya se ha publicado una reseña para este usuario.", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.userName.setOnClickListener {
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("sellerId", contactId)
            startActivity(intent)
        }

        binding.sellerImage.setOnClickListener {
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("sellerId", contactId)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("¿Seguro que quieres borrar el servicio?")
                .setPositiveButton("Sí") { _, _ ->
                    FirebaseFunctions.deleteServiceById(serviceId, this)
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
                    contactId.toString(),
                    serviceId
                ) { chatExists ->
                    if (!chatExists) {
                        firebaseAuth.currentUser?.let { currentUser ->
                            FirebaseFunctions.createChat(
                                currentUser.uid,
                                contactId.toString(),
                                serviceId
                            ) { chatId ->
                                if (chatId != null) {
                                    val intent = Intent(this, ChatActivity::class.java)
                                    intent.putExtra("chatId", chatId)
                                    intent.putExtra("toUser", contactId)
                                    intent.putExtra("fromUser", currentUser.uid)
                                    intent.putExtra("relatedProduct", serviceId)
                                    startActivity(intent)
                                }
                            }
                        }
                    } else {
                        GlobalFunctions.showInfoDialog(this, "Error", "El chat ya existe.")
                    }
                }
            }
        }

        reviewButton.setOnClickListener {
            showReviewDialog()
        }

        binding.favButton.setOnClickListener {
            firebaseAuth = FirebaseAuth.getInstance()
            val userId = firebaseAuth.currentUser?.uid.toString()
            val fav = Favourite("", userId, serviceId, false)
            FirebaseFunctions.favouriteExists(serviceId) { exists ->
                if (exists) {
                    GlobalFunctions.showInfoDialog(
                        this,
                        "Error.",
                        "El servicio ya está en favoritos."
                    )
                } else {
                    if (FirebaseFunctions.addFavourite(fav) != null) {
                        GlobalFunctions.showInfoDialog(
                            this,
                            "¡Favorito añadido!",
                            "Se ha añadido el servicio a favoritos."
                        )
                    } else {
                        GlobalFunctions.showInfoDialog(
                            this,
                            "Error.",
                            "Hubo un problema al añadir el servicio a favoritos."
                        )
                    }
                }
            }
            true
        }
    }
}