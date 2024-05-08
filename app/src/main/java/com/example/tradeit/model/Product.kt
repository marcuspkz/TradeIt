package com.example.tradeit.model

data class Product (
    private var productId: String,
    private var title: String,
    private val description: String,
    private val category: String,
    private val ubication: String,
    private val price: Int,
    private val image: String,
    private val seller: String?,
    private val sellerId: String?,
    private val postingDate: String
) {
    constructor() : this("", "", "", "", "", 0, "", "", "", "")

    fun getProductId(): String {
        return productId
    }

    fun getTitle(): String {
        return title
    }

    fun getDescription(): String {
        return description
    }

    fun getCategory(): String {
        return category
    }

    fun getUbication(): String {
        return ubication
    }

    fun getPrice(): Int {
        return price
    }

    fun getImage(): String {
        return image
    }

    fun getSeller(): String? {
        return seller
    }

    fun getSellerId(): String? {
        return sellerId
    }

    fun getPostingDate(): String {
        return postingDate
    }
}