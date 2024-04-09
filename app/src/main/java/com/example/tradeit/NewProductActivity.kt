package com.example.tradeit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tradeit.databinding.ActivityNewProductBinding
import com.example.tradeit.model.Product
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class NewProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewProductBinding
    private lateinit var firebase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()

        val titleET = binding.productTitleET
        val descriptionET = binding.productDescriptionET
        val categoryET = binding.productCategoryET
        val priceET = binding.productPriceET
        val addButton = binding.addButton

        addButton.setOnClickListener {
            val title = titleET.text.toString()
            val description = descriptionET.text.toString()
            val category = categoryET.text.toString()
            val priceString = priceET.text.toString()
            val product = Product(title, description, category, priceString.toFloat(), "", "Vendedor")
            addProduct(product)

            //añadir comprobacion de fallo al insertar
            Toast.makeText(
                baseContext,
                "Insertado $title.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    //repetida, revisar
    private fun addProduct(product: Product) {
        val databaseReference = firebase.reference
        databaseReference.child("Products").push().setValue(product)
    }
}