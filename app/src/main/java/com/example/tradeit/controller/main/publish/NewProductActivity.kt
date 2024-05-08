package com.example.tradeit.controller.main.publish

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityNewProductBinding
import com.google.firebase.database.FirebaseDatabase

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
        val ubicationET = binding.productUbicationET
        val nextButton = binding.nextButton

        val elementsList = mutableListOf(titleET, descriptionET, categoryET, priceET, ubicationET)

        nextButton.setOnClickListener {
            if (GlobalFunctions.allEditTextAreFilled(elementsList)) {
                val priceText = priceET.text.toString()
                try {
                    val price = priceText.toInt()
                    val intent = Intent(this, ImageActivity::class.java)
                    intent.putExtra("title", titleET.text.toString())
                    intent.putExtra("description", descriptionET.text.toString())
                    intent.putExtra("category", categoryET.text.toString())
                    intent.putExtra("price", price.toString())
                    intent.putExtra("ubication", ubicationET.text.toString())
                    startActivity(intent)
                } catch (e: NumberFormatException) {
                    GlobalFunctions.showInfoDialog(this, "Error", "El precio debe ser un número válido.")
                }
            } else {
                GlobalFunctions.showInfoDialog(this, "Error", "Es necesario completar todos los campos para continuar.")
            }
        }
    }
}