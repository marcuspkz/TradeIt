package com.example.tradeit.controller.main.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tradeit.R
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityChatBinding
import com.example.tradeit.databinding.ActivityProductDetailBinding
import com.example.tradeit.model.chat.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ChatActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseDatabase
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()

        var toUserId = ""
        val chatId = intent.getStringExtra("chatId")
        val sellerId = intent.getStringExtra("toUser").toString()
        val productId = intent.getStringExtra("relatedProduct").toString()
        val fromUserId = intent.getStringExtra("fromUser").toString()
        val testChat = binding.testChat
        val toUserName = binding.toUserName
        val toUserImage = binding.toUserImage
        val toUserProduct = binding.toUserProduct
        val sendButton = binding.sendButton

        FirebaseFunctions.getUserProfilePicture(sellerId) { profilePictureUrl ->
            profilePictureUrl?.let {
                Picasso.get().load(it).into(toUserImage)
            }
        }
        FirebaseFunctions.getProduct(productId) { product ->
            if (product != null) {
                toUserProduct.text = product.getTitle()
            }
        }
        FirebaseFunctions.getUserById(sellerId) { user ->
            if (user != null) {
                toUserId = user.userId
                toUserName.text = user.displayName
            } else {
                // Manejar el caso donde no se pudo obtener el usuario
            }
        }

        FirebaseFunctions.getChatMessages("chatId") { messageList ->
            if (messageList != null) {
                for (message in messageList) {
                    if (testChat.text != "") {
                        val addMessage = "${testChat.text}" + "${message.message}\n"
                        testChat.text = addMessage
                    } else {
                        testChat.text = "${message.fromUser}\n"
                    }
                }
            } else {
                // Hubo un error al obtener los mensajes del chat
            }
        }

        sendButton.setOnClickListener {
            val message = Message(
                FirebaseAuth.getInstance().currentUser?.uid,
                toUserId, binding.message.text.toString(),
                System.currentTimeMillis()
            )
            if (chatId != null) {
                FirebaseFunctions.sendMessage(chatId, message)
            }
        }
    }
}