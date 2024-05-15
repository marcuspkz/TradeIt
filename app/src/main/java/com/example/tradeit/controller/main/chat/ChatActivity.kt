package com.example.tradeit.controller.main.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.R
import com.example.tradeit.controller.adapter.MessageAdapter
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityChatBinding
import com.example.tradeit.model.chat.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ChatActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseDatabase
    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()

        var toUserId = ""
        val chatId = intent.getStringExtra("chatId").toString()
        val sellerId = intent.getStringExtra("toUser").toString()
        val productId = intent.getStringExtra("relatedProduct").toString()
        val fromUserId = intent.getStringExtra("fromUser").toString()
        val testChat = binding.testChat
        val toUserName = binding.toUserName
        val toUserImage = binding.toUserImage
        val toUserProduct = binding.toUserProduct
        val sendButton = binding.sendButton

        messageAdapter = MessageAdapter(mutableListOf())
        binding.rvChat.setHasFixedSize(true)
        binding.rvChat.layoutManager = LinearLayoutManager(this)
        binding.rvChat.adapter = messageAdapter

        FirebaseFunctions.getChatMessages(chatId, messageAdapter) { messagesNo ->
            binding.rvChat.scrollToPosition(messagesNo - 1)
        }

        FirebaseFunctions.getUserProfilePicture(sellerId) { profilePictureUrl ->
            profilePictureUrl?.let {
                Picasso.get().load(it).into(toUserImage)
            }
        }
        FirebaseFunctions.getProduct(productId) { product ->
            if (product != null) {
                toUserProduct.text = product.title
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

        sendButton.setOnClickListener {
            if (binding.message.toString() != "") {
                val message = Message(
                    FirebaseAuth.getInstance().currentUser?.uid,
                    toUserId, binding.message.text.toString(),
                    System.currentTimeMillis()
                )
                if (chatId != null) {
                    FirebaseFunctions.sendMessage(chatId, message)
                    binding.message.setText("")
                }
            }
        }
    }
}