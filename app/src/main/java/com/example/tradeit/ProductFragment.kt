package com.example.tradeit

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.adapter.ProductAdapter
import com.example.tradeit.databinding.FragmentProductBinding
import com.example.tradeit.model.Product
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var firebase: FirebaseDatabase

    //se inicializan variables globales
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProductBinding.inflate(layoutInflater)
        firebase = FirebaseDatabase.getInstance()
    }

    //se inicializa el layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    //se implementa la funcionalidad
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        getAllProducts()
        //generateTestData()
    }

    private fun initUI() {
        binding.searchViewProducts.setOnQueryTextListener(object: SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getProductsByTitle(query.orEmpty())
                return false
            }
            override fun onQueryTextChange(newText: String?) = false
        })

        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(), NewProductActivity::class.java)
            startActivity(intent)
        }

        productAdapter = ProductAdapter()
        binding.rvProducts.setHasFixedSize(true)
        binding.rvProducts.layoutManager = LinearLayoutManager(context)
        binding.rvProducts.adapter = productAdapter
    }

    private fun addProduct(product: Product) {
        val databaseReference = firebase.reference
        databaseReference.child("Products").push().setValue(product)
    }

    private fun getAllProducts() {
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

    private fun getProductsByTitle(title: String) {
        val databaseReference = firebase.reference.child("Products")
        var searchTerm = title.lowercase()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()

                for (data in snapshot.children) {
                    val product = data.getValue(Product::class.java)
                    product?.let {
                        if (removeAccents(it.title).lowercase().contains(searchTerm)) {
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

    private fun removeAccents(text: String): String {
        return text
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
    }

    private fun generateTestData() {
        val product1 = Product(
            "Smartphone Samsung Galaxy S21",
            "El último smartphone de Samsung con increíble rendimiento y cámara de alta resolución.",
            "Electrónicos",
            999.99f,
            "https://ejemplo.com/imagen1.jpg",
            "Samsung Store"
        )

        val product2 = Product(
            "Portátil HP Pavilion",
            "Potente portátil con procesador Intel Core i7 y pantalla Full HD de 15.6 pulgadas.",
            "Informática",
            849.99f,
            "https://ejemplo.com/imagen2.jpg",
            "HP Store"
        )

        val product3 = Product(
            "Zapatillas Nike Air Max",
            "Zapatillas deportivas con tecnología de amortiguación Air Max para mayor comodidad.",
            "Moda",
            129.99f,
            "https://ejemplo.com/imagen3.jpg",
            "Nike Store"
        )

        val product4 = Product(
            "Televisor LG OLED 4K",
            "Televisor con tecnología OLED y resolución 4K para una experiencia visual impresionante.",
            "Electrónicos",
            1499.99f,
            "https://ejemplo.com/imagen4.jpg",
            "LG Store"
        )

        addProduct(product1)
        addProduct(product2)
        addProduct(product3)
        addProduct(product4)
    }
}