package com.example.tradeit.model

data class Product (
    //var id_product: String,
    var title: String,
    val description: String,
    val category: String,
    val ubication: String,
    val price: Float,
    val image: String,
    val seller: String,
) {
    constructor() : this("", "", "", "", 0.0f, "", "")

    public fun setNewTitle(title: String) {
        this.title = title;
    }
}