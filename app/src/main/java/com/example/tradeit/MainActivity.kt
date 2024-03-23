package com.example.tradeit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tradeit.model.Product
import com.google.firebase.*
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //inicializar firebase
        firebase = FirebaseDatabase.getInstance()

        //productos de prueba
        val product1 = Product(
            "Producto 1",
            "Descripción del producto 1",
            "Categoría 1",
            50.0f,
            "url_de_la_imagen_1",
            "Vendedor 1"
        )

        val product2 = Product(
            "Producto 2",
            "Descripción del producto 2",
            "Categoría 2",
            100.0f,
            "url_de_la_imagen_2",
            "Vendedor 2"
        )

        addProduct(product1)
        addProduct(product2)
    }

    private fun addProduct(product: Product) {
        val databaseReference = firebase.reference
        databaseReference.child("Products").push().setValue(product)
    }
}