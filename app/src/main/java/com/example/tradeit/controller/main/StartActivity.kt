package com.example.tradeit.controller.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.pruebalayout.FragmentPageAdapter
import com.example.tradeit.R
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
                    if (tab.position == 1) {
                        tabLayout.backgroundTintList = ContextCompat.getColorStateList(this@StartActivity, R.color.blue_clear)
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this@StartActivity, R.color.blue_dark))
                        tabLayout.setTabTextColors(
                            ContextCompat.getColor(this@StartActivity, R.color.gray), //pestaña no seleccionada
                            ContextCompat.getColor(this@StartActivity, R.color.blue_dark)
                        )
                    } else {
                        tabLayout.backgroundTintList = ContextCompat.getColorStateList(this@StartActivity, R.color.main_bg)
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this@StartActivity, R.color.main))
                        tabLayout.setTabTextColors(
                            ContextCompat.getColor(this@StartActivity, R.color.gray), //pestaña no seleccionada
                            ContextCompat.getColor(this@StartActivity, R.color.main_dark)
                        )
                    }
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