package com.example.tradeit.model.chat

import java.util.Date

data class Message(
    val fromUser: String?,
    val toUser: String?,
    val message: String,
    val sendDate: Long,
) {
    constructor() : this("", "", "", 0)
}