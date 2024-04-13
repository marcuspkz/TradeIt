package com.example.tradeit.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tradeit.controller.FirebaseFunctions
import com.example.tradeit.databinding.FragmentUserBinding
import com.google.firebase.database.FirebaseDatabase

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var firebase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUserBinding.inflate(layoutInflater)
        firebase = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI() {
        binding.mailText.text = FirebaseFunctions.getEmail()
        binding.displayNameText.text = FirebaseFunctions.getDisplayName()
    }
}