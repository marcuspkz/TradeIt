package com.example.tradeit.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.controller.FirebaseFunctions
import com.example.tradeit.controller.adapter.ProductAdapter
import com.example.tradeit.databinding.FragmentProductBinding
import com.example.tradeit.databinding.FragmentServiceBinding
import com.google.firebase.database.FirebaseDatabase

class ServiceFragment : Fragment() {
    private lateinit var binding: FragmentServiceBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var firebase: FirebaseDatabase

    //se inicializan variables globales
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentServiceBinding.inflate(layoutInflater)
        firebase = FirebaseDatabase.getInstance()
    }

    //se inicializa el layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    //se implementa la funcionalidad
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}