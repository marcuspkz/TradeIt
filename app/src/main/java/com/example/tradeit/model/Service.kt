package com.example.tradeit.model

class Service (
    var serviceId: String,
    var title: String,
    val description: String,
    val category: String,
    val price: String,
    val image: String,
    val contact: String?,
    val contactId: String?,
    val duration: Int,
    val location: String,
    val requirements: String,
    val postingDate: String
    ) {
        constructor() : this("", "", "", "", "", "", "", "", 0, "", "", "")
}