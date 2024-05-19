package com.example.tradeit.model

data class Favourite(
    var favId: String,
    var userId: String,
    var itemId: String?,
    var isProduct: Boolean,
) {
    constructor() : this("", "", null, true)
}