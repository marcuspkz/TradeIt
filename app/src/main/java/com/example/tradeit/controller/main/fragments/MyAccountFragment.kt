package com.example.tradeit.controller.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.FragmentMyaccountBinding
import com.google.firebase.database.FirebaseDatabase

class MyAccountFragment : Fragment() {
    private lateinit var binding: FragmentMyaccountBinding
    private lateinit var firebase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyaccountBinding.inflate(layoutInflater)
        firebase = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyaccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI() {
        binding.displayName.text = FirebaseFunctions.getDisplayName()
    }
}