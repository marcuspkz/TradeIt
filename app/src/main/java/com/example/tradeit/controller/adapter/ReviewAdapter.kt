package com.example.tradeit.controller.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.R
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
        var firebaseAuth: FirebaseAuth
        holder.bind(reviewList[position])
        holder.itemView.setOnLongClickListener {
            firebaseAuth = FirebaseAuth.getInstance()
            val reviewId = reviewList[position].reviewId
            val userId = reviewList[position].profileId

            if (userId != null) {
                FirebaseFunctions.deleteReviewById(reviewId, userId) { success, message ->
                    if (success) {
                        Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()
                        reviewList.removeAt(position)
                        notifyItemRemoved(position)
                    } else {
                        GlobalFunctions.showInfoDialog(holder.itemView.context, "Error", message)
                    }
                }
            } else {
                GlobalFunctions.showInfoDialog(holder.itemView.context, "Error", "Usuario no autenticado.")
            }
            true
        }
    }
    override fun getItemCount() = reviewList.size
}