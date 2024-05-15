package com.example.tradeit.controller.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.controller.adapter.ProductAdapter
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.main.publish.NewProductActivity
import com.example.tradeit.databinding.FragmentProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProductBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        FirebaseFunctions.getAllProducts(productAdapter)
    }

    override fun onResume() {
        super.onResume()
        FirebaseFunctions.getAllProducts(productAdapter)
    }

    private fun initUI() {
        binding.searchViewProducts.setOnQueryTextListener(object: SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                FirebaseFunctions.getProductsByTitle(query.orEmpty(), productAdapter)
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
}