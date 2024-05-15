package com.example.tradeit.controller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.R
import com.example.tradeit.model.chat.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private var messageList: MutableList<Message>) : RecyclerView.Adapter<MessageViewHolder>() {
    fun addMessage(message: Message) {
        messageList.add(message)
        notifyItemInserted(messageList.size - 1)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        when (viewType) {
            0 -> return MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
            )
            1 -> return MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_message_right, parent, false)
            )
        }
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_message_right, parent, false)
        )
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemViewType(position: Int): Int {
        if (messageList[position].fromUser == FirebaseAuth.getInstance().currentUser?.uid) {
            return 0;
        } else {
            return 1;
        }
    }
    override fun getItemCount() = messageList.size
}