package com.example.pruebalayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tradeit.controller.main.fragments.ChatFragment
import com.example.tradeit.controller.main.fragments.FavouritesFragment
import com.example.tradeit.controller.main.fragments.ProductFragment
import com.example.tradeit.controller.main.fragments.ServiceFragment
import com.example.tradeit.controller.main.fragments.MyAccountFragment

class FragmentPageAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductFragment()
            //1 -> ServiceFragment()
            1 -> FavouritesFragment()
            2 -> ChatFragment()
            3 -> MyAccountFragment()
            else -> throw IllegalArgumentException("Posición no válida: $position")
        }
    }
}
