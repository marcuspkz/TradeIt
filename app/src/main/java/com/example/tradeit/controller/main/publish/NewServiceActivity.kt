package com.example.tradeit.controller.main.publish

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.text.isDigitsOnly
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityNewProductBinding
import com.example.tradeit.databinding.ActivityNewServiceBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat

class NewServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewServiceBinding
    private lateinit var firebase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()

        val titleET = binding.productTitleET
        val descriptionET = binding.productDescriptionET
        val priceET = binding.productPriceET
        val priceSpinner = binding.priceSpinner
        val durationRS = binding.durationRS
        var duration = 0
        val category = binding.categorySpinner
        val ubicationET = binding.autoCompleteCity
        val requirements = binding.requirements
        val nextButton = binding.nextButton
        val elementsList = mutableListOf(titleET, descriptionET, priceET, ubicationET, requirements)

        priceET.isSingleLine = true
        titleET.isSingleLine = true
        ubicationET.isSingleLine = true

        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, GlobalFunctions.cities)
        ubicationET.setAdapter(adapter)
        ubicationET.threshold = 1

        durationRS.addOnChangeListener { _, value, _ ->
            val df = DecimalFormat("#")
            duration = df.format(value).toInt()
            binding.textRS.text = "$duration horas"
        }

        nextButton.setOnClickListener {
            if (GlobalFunctions.allEditTextAreFilled(elementsList)) {
                var priceText = ""
                if (priceSpinner.selectedItem.toString() == "Precio por hora") {
                    priceText = priceET.text.toString() + "€/hora"
                } else {
                    priceText = priceET.text.toString() + "€"
                }
                if (priceET.text.toString().isDigitsOnly()) {
                    val intent = Intent(this, ImageActivity::class.java)
                    intent.putExtra("title", titleET.text.toString())
                    intent.putExtra("description", descriptionET.text.toString())
                    intent.putExtra("category", category.selectedItem.toString())
                    intent.putExtra("price", priceText)
                    intent.putExtra("duration", duration.toString())
                    intent.putExtra("ubication", ubicationET.text.toString())
                    intent.putExtra("requirements", requirements.text.toString())
                    intent.putExtra("isProduct", "false")
                    startActivity(intent)
                } else {
                    GlobalFunctions.showInfoDialog(
                        this,
                        "Error",
                        "El precio debe ser un número válido."
                    )
                }
            } else {
                GlobalFunctions.showInfoDialog(
                    this,
                    "Error",
                    "Es necesario completar todos los campos para continuar."
                )
            }
        }
    }
}