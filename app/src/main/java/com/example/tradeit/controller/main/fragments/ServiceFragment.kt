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
import com.example.tradeit.controller.adapter.ServiceAdapter
import com.example.tradeit.controller.main.publish.NewProductActivity
import com.example.tradeit.controller.main.publish.NewServiceActivity
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.FragmentServiceBinding
import com.google.firebase.database.FirebaseDatabase

class ServiceFragment : Fragment() {
    private lateinit var binding: FragmentServiceBinding
    private lateinit var serviceAdapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentServiceBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        FirebaseFunctions.getAllServices(serviceAdapter)
    }

    override fun onResume() {
        super.onResume()
        FirebaseFunctions.getAllServices(serviceAdapter)
    }

    private fun initUI() {
        binding.searchViewServices.setOnQueryTextListener(object: SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                FirebaseFunctions.getServiceByTitle(query.orEmpty(), serviceAdapter)
                return false
            }
            override fun onQueryTextChange(newText: String?) = false
        })

        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(), NewServiceActivity::class.java)
            startActivity(intent)
        }

        serviceAdapter = ServiceAdapter()
        binding.rvServices.setHasFixedSize(true)
        binding.rvServices.layoutManager = LinearLayoutManager(context)
        binding.rvServices.adapter = serviceAdapter
    }
}