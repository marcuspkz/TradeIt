package com.example.tradeit.controller.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.controller.statics.AESCrypt
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ItemChatBinding
import com.example.tradeit.databinding.ItemReviewBinding
import com.example.tradeit.model.Review
import com.example.tradeit.model.chat.Chat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemChatBinding.bind(view)
    private val key = "aaaaaaaaaaaaaaaa"
    private lateinit var firebase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth

    fun bind(chat: Chat) {
        firebase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        if (chat.fromUser == firebaseAuth.currentUser?.uid) {
            FirebaseFunctions.getUserById(chat.toUser) { user ->
                if (user != null) {
                    binding.userTitle.text = user.displayName
                    //imagen de perfil
                    FirebaseFunctions.getUserProfilePicture(user.userId) { profilePictureUrl ->
                        profilePictureUrl?.let {
                            Picasso.get().load(it).into(binding.ivImage)
                        }
                    }
                } else {
                    //error usuario
                }
            }
        } else if (chat.toUser == firebaseAuth.currentUser?.uid) {
            FirebaseFunctions.getUserById(chat.fromUser) { user ->
                if (user != null) {
                    binding.userTitle.text = user.displayName
                    //imagen de perfil
                    FirebaseFunctions.getUserProfilePicture(user.userId) { profilePictureUrl ->
                        profilePictureUrl?.let {
                            Picasso.get().load(it).into(binding.ivImage)
                        }
                    }
                } else {
                    //error usuario
                }
            }
        }

        FirebaseFunctions.getLastMessage(chat.chatId) { lastMessage ->
            if (lastMessage != null) {
                binding.lastMessage.text = AESCrypt.decrypt(lastMessage.message, key)
            } else {
                //no hay last message
                binding.lastMessage.text = "¡Inicia una conversación!"
            }
        }
    }
}