package com.example.tradeit.controller.main.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.AboutActivity
import com.example.tradeit.controller.adapter.ReviewAdapter
import com.example.tradeit.controller.main.AccountInfoActivity
import com.example.tradeit.controller.main.MainActivity
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.FragmentMyaccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class MyAccountFragment : Fragment() {
    private lateinit var binding: FragmentMyaccountBinding
    private lateinit var firebase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyaccountBinding.inflate(layoutInflater)
        firebase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.currentUser?.let {
            FirebaseFunctions.getUserProfilePicture(it.uid) { profilePictureUrl ->
                profilePictureUrl?.let {
                    Picasso.get().load(it).into(binding.userImage)
                }
            }
        }
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

    override fun onResume() {
        super.onResume()
        FirebaseAuth.getInstance().currentUser?.let { FirebaseFunctions.getAllReviewsForUser(it.uid, reviewAdapter) }
    }

    private fun initUI() {
        reviewAdapter = ReviewAdapter(mutableListOf())
        binding.rvUserProducts.setHasFixedSize(true)
        binding.rvUserProducts.layoutManager = LinearLayoutManager(context)
        binding.rvUserProducts.adapter = reviewAdapter

        binding.displayName.text = FirebaseFunctions.getDisplayName(false)
        FirebaseAuth.getInstance().currentUser?.let {
            FirebaseFunctions.averageRating(it.uid) { avgRating ->
                if (avgRating != 0.0) {
                    binding.rating.text = "Valoración media: ${String.format("%.1f", avgRating)} ★"
                } else {
                    binding.rating.text = "Este usuario no tiene valoraciones."
                }
            }
        }

        FirebaseAuth.getInstance().currentUser?.let { FirebaseFunctions.getAllReviewsForUser(it.uid, reviewAdapter) }

        binding.infoButton.setOnClickListener {
            val intent = Intent(requireContext(), AccountInfoActivity::class.java)
            startActivity(intent)
        }

        binding.aboutButton.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }

        binding.signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(), "¡Hasta la próxima!", Toast.LENGTH_SHORT).show()
        }
    }
}