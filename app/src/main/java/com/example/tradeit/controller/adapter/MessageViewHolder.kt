package com.example.tradeit.controller.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ItemMessageBinding
import com.example.tradeit.databinding.ItemProductBinding
import com.example.tradeit.model.chat.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMessageBinding.bind(view)

    fun bind(message: Message) {
        binding.textMessage.text = message.message
        binding.postingDate.text = message.sendDate.toString()
    }
}