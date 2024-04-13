package com.example.tradeit.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.pruebalayout.FragmentPageAdapter
import com.example.tradeit.controller.adapter.ProductAdapter
import com.example.tradeit.databinding.ActivityStartBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.FirebaseDatabase

class StartActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter
    private lateinit var firebase: FirebaseDatabase
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        firebase = FirebaseDatabase.getInstance()
        setContentView(binding.root)

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2

        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}