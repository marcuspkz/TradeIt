package com.example.tradeit.controller.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.controller.adapter.FavouriteAdapter
import com.example.tradeit.controller.adapter.ProductAdapter
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.FragmentFavouritesBinding
import com.example.tradeit.databinding.FragmentMyaccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favouriteAdapter: FavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        FirebaseFunctions.getFavourites(FirebaseAuth.getInstance().currentUser?.uid, favouriteAdapter)
    }

    override fun onResume() {
        super.onResume()
        FirebaseFunctions.getFavourites(FirebaseAuth.getInstance().currentUser?.uid, favouriteAdapter)
        FirebaseAuth.getInstance().currentUser?.let {
            FirebaseFunctions.favouritesNo() {favsNo ->
                if (favsNo == 0) {
                    binding.infoFavText.visibility = VISIBLE
                } else {
                    binding.infoFavText.visibility = GONE
                }
            }
        }


    }

    private fun initUI() {
        binding.infoFavText.visibility = GONE
        favouriteAdapter = FavouriteAdapter(mutableListOf())
        binding.rvFavourites.setHasFixedSize(true)
        binding.rvFavourites.layoutManager = LinearLayoutManager(context)
        binding.rvFavourites.adapter = favouriteAdapter
    }
}