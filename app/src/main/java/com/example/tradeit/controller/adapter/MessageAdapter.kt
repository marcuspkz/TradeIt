package com.example.tradeit.controller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.R
import com.example.tradeit.model.chat.Message

class MessageAdapter(private var messageList: MutableList<Message>) : RecyclerView.Adapter<MessageViewHolder>() {
    fun updateList(list: MutableList<Message>) {
        messageList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        )
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }
    override fun getItemCount() = messageList.size
}