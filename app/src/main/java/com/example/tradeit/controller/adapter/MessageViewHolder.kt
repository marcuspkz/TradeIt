package com.example.tradeit.controller.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.controller.statics.AESCrypt
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ItemMessageBinding
import com.example.tradeit.databinding.ItemProductBinding
import com.example.tradeit.model.chat.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import javax.crypto.SecretKey

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMessageBinding.bind(view)

    fun bind(message: Message) {
        val key = "aaaaaaaaaaaaaaaa"
        binding.textMessage.text = (AESCrypt.decrypt(message.message, key))
        binding.postingDate.text = formatDate(message.sendDate)
    }

    private fun formatDate(milliseconds: Long): String {
        val date = Date(milliseconds)
        val formatter = SimpleDateFormat("dd/MM/yy - HH:mm")
        return formatter.format(date)
    }
}