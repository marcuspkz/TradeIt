package com.example.tradeit.controller.main.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.R
import com.example.tradeit.controller.adapter.MessageAdapter
import com.example.tradeit.controller.main.detail.UserDetailActivity
import com.example.tradeit.controller.statics.AESCrypt
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

        /*esto es para determinar qué cabecera sale en el chat
        * si no se pone, si te abren chat, saldrá la del otro usuario
        * a partir de aqui, obtainDataId tiene al usuario de enfrente*/
        var obtainDataId = ""
        if (sellerId == FirebaseAuth.getInstance().currentUser?.uid) {
            obtainDataId = fromUserId
        } else {
            obtainDataId = sellerId
        }

        FirebaseFunctions.getUserProfilePicture(obtainDataId) { profilePictureUrl ->
            profilePictureUrl?.let {
                Picasso.get().load(it).into(toUserImage)
            }
        }

        FirebaseFunctions.getProduct(productId) { product ->
            if (product != null) {
                toUserProduct.text = product.title
            }
        }
        FirebaseFunctions.getUserById(obtainDataId) { user ->
            if (user != null) {
                toUserId = user.userId
                toUserName.text = user.displayName
            } else {
                //no se pudo obtener el usuario
                GlobalFunctions.showInfoDialog(this, "Error", "No se pudo obtener el usuario")
            }
        }

        sendButton.setOnClickListener {
            var key = "aaaaaaaaaaaaaaaa"
            if (binding.message.toString() != "") {
                val message = Message(
                    FirebaseAuth.getInstance().currentUser?.uid,
                    toUserId, AESCrypt.encrypt(binding.message.text.toString(), key),
                    System.currentTimeMillis()
                )
                if (chatId != null) {
                    FirebaseFunctions.sendMessage(chatId, message)
                    binding.message.setText("")
                }
            }
        }

        binding.toUserImage.setOnClickListener {
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("sellerId", obtainDataId)
            startActivity(intent)
        }

        binding.toUserName.setOnClickListener {
            val intent = Intent(this, UserDetailActivity::class.java)
            intent.putExtra("sellerId", obtainDataId)
            startActivity(intent)
        }
    }
}