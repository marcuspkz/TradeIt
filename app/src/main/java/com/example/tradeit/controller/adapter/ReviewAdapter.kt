package com.example.tradeit.controller.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.R
import com.example.tradeit.controller.main.detail.UserDetailActivity
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.model.Review
import com.google.firebase.auth.FirebaseAuth

class ReviewAdapter(private var reviewList: MutableList<Review>) : RecyclerView.Adapter<ReviewViewHolder>() {
    fun updateList(list: MutableList<Review>) {
        reviewList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
        holder.itemView.setOnClickListener {
            val context = it.context
            AlertDialog.Builder(context)
                .setTitle("Confirmación")
                .setMessage("¿Visitar el perfil del usuario?")
                .setPositiveButton("Sí") { dialog, _ ->
                    val intent = Intent(context, UserDetailActivity::class.java)
                    intent.putExtra("sellerId", reviewList[position].publisherId)
                    context.startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        holder.itemView.setOnLongClickListener {
            val context = it.context
            val reviewId = reviewList[position].reviewId
            val userId = reviewList[position].profileId

            if (userId != null) {
                FirebaseFunctions.checkReviewDeletionPermission(reviewId, userId) { canDelete, message ->
                    if (canDelete) {
                        AlertDialog.Builder(context)
                            .setTitle("Confirmación")
                            .setMessage("¿Seguro que quieres eliminar esta reseña?")
                            .setPositiveButton("Sí") { dialog, _ ->
                                FirebaseFunctions.deleteReviewById(reviewId, userId) { success, deleteMessage ->
                                    if (success) {
                                        Toast.makeText(context, deleteMessage, Toast.LENGTH_SHORT).show()
                                        reviewList.removeAt(position)
                                        notifyItemRemoved(position)
                                    } else {
                                        GlobalFunctions.showInfoDialog(context, "Error", deleteMessage)
                                    }
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    } else {
                        GlobalFunctions.showInfoDialog(context, "Error", message)
                    }
                }
            } else {
                GlobalFunctions.showInfoDialog(context, "Error", "Usuario no autenticado.")
            }
            true
        }
    }
    override fun getItemCount() = reviewList.size
}