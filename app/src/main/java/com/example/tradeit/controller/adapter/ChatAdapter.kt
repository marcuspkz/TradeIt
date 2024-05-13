package com.example.tradeit.controller.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.R
import com.example.tradeit.controller.main.chat.ChatActivity
import com.example.tradeit.model.Review
import com.example.tradeit.model.chat.Chat

class ChatAdapter(private var chatList: List<Chat> = emptyList()) : RecyclerView.Adapter<ChatViewHolder>() {
    fun updateList(list: List<Chat>) {
        chatList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatList[position])
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("chatId", chatList[position].chatId)
            intent.putExtra("toUser", chatList[position].toUser)
            intent.putExtra("fromUser", chatList[position].fromUser)
            intent.putExtra("relatedProduct", chatList[position].relatedProduct)
            context.startActivity(intent)
        }
    }
    override fun getItemCount() = chatList.size
}