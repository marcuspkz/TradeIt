package com.example.tradeit.controller

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.tradeit.controller.adapter.ProductAdapter
import com.example.tradeit.model.Product
import com.example.tradeit.controller.main.MainActivity
import com.example.tradeit.controller.main.RegisterUserActivity
import com.example.tradeit.controller.main.StartActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseFunctions {
    //añadir producto
    fun addProduct(product: Product, firebase: FirebaseDatabase): String {
        val databaseReference = firebase.reference
        val productRef = databaseReference.child("Products").push()
        val productId = productRef.key.toString()
        productRef.setValue(product)
        return productId
    }

    //modificar imagen producto
    fun modifyProductImage(productId: String, image: String, firebase: FirebaseDatabase) {
        val databaseReference = firebase.reference
        val productRef = databaseReference.child("Products").child(productId)
        productRef.child("image").setValue(image)
    }

    //obtener display name
    fun getDisplayName(): String? {
        val user = Firebase.auth.currentUser
        var name: String?
        name = ""
        user?.let {
            for (profile in it.providerData) {
                name = profile.displayName
            }
        }

        return name
    }

    //obtener email
    fun getEmail(): String? {
        val user = Firebase.auth.currentUser
        var name: String?
        name = ""
        user?.let {
            for (profile in it.providerData) {
                name = profile.email
            }
        }

        return name
    }

    //login usuario
    fun loginUser(email: String, password: String, firebaseAuth: FirebaseAuth, context: Context) {
        val email = email
        val password = password

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as MainActivity) { task ->
                if (task.isSuccessful) {
                    //autenticación exitosa
                    val intent = Intent(context, StartActivity::class.java)
                    context.startActivity(intent)
                    context.finish() //cerrar esta actividad para que no se pueda volver atrás
                    var displayName = FirebaseFunctions.getDisplayName()
                    Toast.makeText(
                        context,
                        "¡Bienvenido, $displayName!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //autenticación fallida
                    Toast.makeText(
                        context,
                        "Credenciales incorrectas. Revisa los datos e inténtalo de nuevo.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //registrar usuario
    fun registerUser(displayName: String, email: String, password: String, firebaseAuth: FirebaseAuth, context: Context) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as RegisterUserActivity) { task ->
                if (task.isSuccessful) {
                    //registro exitoso
                    updateDisplayName(displayName)
                    Toast.makeText(
                        context,
                        "¡Registro de usuario $displayName exitoso!",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    //registro fallido
                    Toast.makeText(
                        context,
                        "La autenticación ha fallado.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    //actualizar display name
    private fun updateDisplayName(name: String) {
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = name
            //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")   //esto es para ponerle una foto de perfil
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //actualización exitosa
                    Log.d(ContentValues.TAG, "User profile updated.")
                }
            }
    }

    //obtener listado completo de productos
    fun getAllProducts(firebase: FirebaseDatabase, productAdapter: ProductAdapter) {
        val databaseReference = firebase.reference.child("Products")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()

                for (data in snapshot.children) {
                    val product = data.getValue(Product::class.java)
                    product?.let {
                        productList.add(it)
                    }
                }

                productAdapter.updateList(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error de lectura de la base de datos
            }
        })
    }

    //obtener productos por título
    fun getProductsByTitle(title: String, firebase: FirebaseDatabase, productAdapter: ProductAdapter) {
        val databaseReference = firebase.reference.child("Products")
        var searchTerm = title.lowercase()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()

                for (data in snapshot.children) {
                    val product = data.getValue(Product::class.java)
                    product?.let {
                        if (GlobalFunctions.removeAccents(it.title).lowercase().contains(searchTerm)) {
                            productList.add(it)
                        }
                    }
                }

                productAdapter.updateList(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error de lectura de la base de datos
            }
        })
    }

    fun generateTestData(firebase: FirebaseDatabase) {
        val product1 = Product(
            "Smartphone Samsung Galaxy S21",
            "El último smartphone de Samsung con increíble rendimiento y cámara de alta resolución.",
            "Electrónicos",
            "Zaragoza",
            999.99f,
            "https://ejemplo.com/imagen1.jpg",
            "Samsung Store"
        )

        val product2 = Product(
            "Portátil HP Pavilion",
            "Potente portátil con procesador Intel Core i7 y pantalla Full HD de 15.6 pulgadas.",
            "Informática",
            "Zaragoza",
            849.99f,
            "https://ejemplo.com/imagen2.jpg",
            "HP Store"
        )

        val product3 = Product(
            "Zapatillas Nike Air Max",
            "Zapatillas deportivas con tecnología de amortiguación Air Max para mayor comodidad.",
            "Moda",
            "Zaragoza",
            129.99f,
            "https://ejemplo.com/imagen3.jpg",
            "Nike Store"
        )

        val product4 = Product(
            "Televisor LG OLED 4K",
            "Televisor con tecnología OLED y resolución 4K para una experiencia visual impresionante.",
            "Electrónicos",
            "Zaragoza",
            1499.99f,
            "https://ejemplo.com/imagen4.jpg",
            "LG Store"
        )

        addProduct(product1, firebase)
        addProduct(product2, firebase)
        addProduct(product3, firebase)
        addProduct(product4, firebase)
    }
}