package com.example.tradeit.model.chat

import java.util.Date

data class Chat(
    val chatId: String,
    val fromUser: String,
    val toUser: String,
    var messages: List<Message>,
    val relatedProduct: String,
    var lastMessage: Message?
) {
    constructor() : this("", "", "", emptyList(), "", null)
}