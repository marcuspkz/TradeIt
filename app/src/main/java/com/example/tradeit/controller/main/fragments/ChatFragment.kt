package com.example.tradeit.controller.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.controller.adapter.ChatAdapter
import com.example.tradeit.controller.main.chat.ChatActivity
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.FragmentChatBinding
import com.example.tradeit.databinding.FragmentMyaccountBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChatBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseFunctions.getUserChats(userId, chatAdapter)
    }

    override fun onResume() {
        super.onResume()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseFunctions.getUserChats(userId, chatAdapter)
    }

    private fun initUI() {
        chatAdapter = ChatAdapter()
        binding.rvChats.setHasFixedSize(true)
        binding.rvChats.layoutManager = LinearLayoutManager(context)
        binding.rvChats.adapter = chatAdapter
    }
}