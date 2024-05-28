package com.example.tradeit.model

data class Product (
    var productId: String,
    var title: String,
    val description: String,
    val category: String,
    val ubication: String,
    val price: Int,
    val image: String,
    val seller: String?,
    val sellerId: String?,
    val postingDate: Long
) {
    constructor() : this("", "", "", "", "", 0, "", "", "", 0)
}