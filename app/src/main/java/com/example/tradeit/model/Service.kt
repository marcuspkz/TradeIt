package com.example.tradeit.model

class Service (
    var id_service: String,
    var title: String,
    val description: String,
    val category: String,
    val price: Float,
    val image: String,
    val contact: String,
    val duration: String,
    val location: String,
    val requirements: String
    ) {
        constructor() : this("", "", "", "", 0.0f, "", "", "", "", "")

        public fun setNewTitle(title: String) {
            this.title = title;
        }
}