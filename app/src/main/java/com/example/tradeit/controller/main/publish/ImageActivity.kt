package com.example.tradeit.controller.main.publish

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.main.start.StartActivity
import com.example.tradeit.databinding.ActivityImageBinding
import com.example.tradeit.model.Product
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage

class ImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageBinding
    private lateinit var firebase: FirebaseDatabase
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebase = FirebaseDatabase.getInstance()

        //recuperar los datos de la otra pantalla
        val title = intent.getStringExtra("title").toString()
        val description = intent.getStringExtra("description").toString()
        val category = intent.getStringExtra("category").toString()
        val price = intent.getStringExtra("price").toString()
        val ubication = intent.getStringExtra("ubication").toString()

        //datos de usuario logueado
        val currentUser = FirebaseAuth.getInstance().currentUser
        val displayName = currentUser?.displayName.toString()

        val imageTitleTV = binding.imageTitle
        imageTitleTV.text = title
        val chooseImgButton = binding.chooseImage
        val publishButton = binding.publishButton
        val imageView = binding.imageView

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageView.setImageURI(it)
                selectedImageUri = it
            }
        }

        fun uploadImage(imageUri: Uri, productId: String, callback: (String) -> Unit): String {
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("product_images/$productId/product_image.jpg")
            var image = ""

            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        image = uri.toString()
                        callback(image)
                    }
                }
                .addOnFailureListener { exception ->
                    callback("error")
                }

            return image
        }

        chooseImgButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        publishButton.setOnClickListener {
            if (selectedImageUri != null) {
                //generamos el producto sin la imagen y sin id, y lo subimos
                //lo del id es sencillo. se inserta, se genera su id de firebase pero luego ese id mi programa también
                //lo tiene que conocer, dado que si no, es imposible identificarlo posteriormente en el adapter
                val product = Product("", title, description, category, ubication, price.toFloat(), "", displayName, "")
                val productId = FirebaseFunctions.addProduct(product, firebase)

                //subimos la imagen y obtenemos la URL
                uploadImage(selectedImageUri!!, productId) {imageUrl ->
                    if (imageUrl == "error") {
                        Toast.makeText(
                            baseContext,
                            "Error al subir la imagen.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        FirebaseFunctions.modifyProductImage(productId, imageUrl, firebase)
                        FirebaseFunctions.setProductId(productId, firebase)
                        Toast.makeText(
                            baseContext,
                            "Producto publicado correctamente.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(this, StartActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "No hay imagen seleccionada.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}