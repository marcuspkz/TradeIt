package com.example.tradeit.model

data class Review(
    var reviewId: String,
    var rating: Int,
    var comment: String,
    var postingDate: String,
    var publisherId: String,
    var profileId: String,
    var productId: String? = null,
    var serviceId: String? = null
) {
    constructor() : this("", 0, "", "", "", "", "")
}
