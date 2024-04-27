package com.example.pruebalayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tradeit.controller.main.ProductFragment
import com.example.tradeit.controller.main.ServiceFragment
import com.example.tradeit.controller.main.MyAccountFragment

class FragmentPageAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductFragment()
            1 -> ServiceFragment()
            2 -> MyAccountFragment()
            3 -> MyAccountFragment()
            4 -> MyAccountFragment()
            else -> throw IllegalArgumentException("Posición no válida: $position")
        }
    }
}
